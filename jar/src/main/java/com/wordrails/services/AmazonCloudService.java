package com.wordrails.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.wordrails.business.File;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import com.wordrails.util.WordrailsUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonCloudService {

	@Value("${amazon.accessKey}")
	String accessKey;
	@Value("${amazon.accessSecretKey}")
	String accessSecretKey;
	@Value("${amazon.distributionKey}")
	String distributionKey;
	@Value("${amazon.privateCloudfrontUrl}")
	String publicCloudfrontUrl;
	@Value("${amazon.publicCloudfrontUrl}")
	String privateCloudfrontUrl;
	@Value("${amazon.publicBucket}")
	String publicBucket;
	@Value("${amazon.privateBucket}")
	String privateBucket;

	public String getPrivateImageURL(String networkDomain, String fileName) throws IOException {
		return "http://" + privateCloudfrontUrl + "/" + networkDomain + "/images/" + fileName;
	}

	public String getPublicImageURL(String networkDomain, String fileName) throws IOException {
		return "http://" + publicCloudfrontUrl + "/" + networkDomain + "/images/" + fileName;
	}

	public void uploadPublicImage(InputStream input, Long lenght, String networkDomain, String fileName) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, publicBucket, fileName);
	}

	public void uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String fileName) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, privateBucket, fileName);
	}

	public String uploadPublicImage(InputStream input, Long lenght, String networkDomain, String size, String mime) throws IOException, AmazonS3Exception {
		byte[] bytes = WordrailsUtil.getBytes(input);
		String hash = WordrailsUtil.getHash(bytes);
		uploadImage(new ByteArrayInputStream(bytes), lenght, networkDomain, publicBucket, hash, size, mime);
		return hash;
	}

	public String uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String size, String mime) throws IOException, AmazonS3Exception {
		byte[] bytes = WordrailsUtil.getBytes(input);
		String hash = WordrailsUtil.getHash(bytes);
		uploadImage(new ByteArrayInputStream(bytes), lenght, networkDomain, privateBucket, hash, size, mime);
		return hash;
	}

	public void uploadPublicImage(InputStream input, Long lenght, String networkDomain, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, publicBucket, fileName, size, mime);
	}

	public void uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, privateBucket, fileName, size, mime);
	}

	private void uploadImage(InputStream input, Long lenght, String networkDomain, String bucketName, String fileName) throws IOException, AmazonS3Exception {
		String path = networkDomain + "/images/" + fileName;
		uploadFile(input, lenght, bucketName, path, null);
	}

	private void uploadImage(InputStream input, Long lenght, String networkDomain, String bucketName, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(mime);
		md.addUserMetadata("size", size);

		String path = networkDomain + "/images/" + fileName;
		uploadFile(input, lenght, bucketName, path, md);
	}

	public boolean exists(AmazonS3 s3Client, String bucket, String key) {
		try {
			s3Client.getObject(bucket, key);
		} catch(AmazonServiceException e) {
			return false;
		}
		return true;
	}

	private void uploadFile(InputStream input, Long lenght, String bucketName, String keyName, ObjectMetadata metadata) throws IOException, AmazonS3Exception {
		if (input.available() == 0) {
			throw new AmazonS3Exception("InputStream is empty");
		}

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		AmazonS3 s3Client = new AmazonS3Client(awsCreds);

		if(exists(s3Client, bucketName, keyName)) {
			return;
		}

		// Create a list of UploadPartResponse objects. You get one of these for
		// each part upload.
		List<PartETag> partETags = new ArrayList<>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
		if(metadata != null) initRequest.setObjectMetadata(metadata);

		initRequest.setCannedACL(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		long partSize = 7 * 1024 * 1024; // Set part size to 7 MB.

		try {
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < lenght; i++) {
				// Last part can be less than 3 MB. Adjust part size.
				partSize = Math.min(partSize, (lenght - filePosition));

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest().
						withBucketName(bucketName).
						withKey(keyName).
						withUploadId(initResponse.getUploadId()).
						withPartNumber(i).
						withFileOffset(filePosition).
						withInputStream(input).
						withPartSize(partSize);

				// Upload part and add response to our list.
				partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId(), partETags);

			s3Client.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));

			throw new AmazonS3Exception("Error uploading file to Amazon S3", e);
		}
	}


	//--------------------------------UPLOAD SCRIPT---------------------------------

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;

	private String uploadFile(String domain, File file, String sizeType) throws SQLException, IOException {
		System.out.println("uploading file " + file.id);

		Blob blob = fileContentsRepository.findOne(file.id).contents;
		InputStream is;
		long byteSize;

		if(blob == null) {
			if(file.url != null) {
				is = WordrailsUtil.getStreamFromUrl(file.url);
				byteSize = ((FileInputStream) is).getChannel().size();
			} else {
				return null;
			}
		} else {
			is = blob.getBinaryStream();
			byteSize = blob.length();
		}



		if(file.mime == null) file.mime = new Tika().detect(is);

		String hash = null;

		try {
			hash = uploadPublicImage(is, byteSize, domain, sizeType, file.mime);
			file.hash = hash;
			fileRepository.save(file);
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		}

		return hash;
	}

	private Image uploadImage(Image image, String domain) throws IOException, SQLException {
		saveFilesHash(image);

		if (image.originalHash != null && image.largeHash != null && image.mediumHash != null && image.smallHash != null) {
			return image;
		}

		String hash;
		if(image.originalHash == null) {
			hash = uploadFile(domain, image.original, "original");
			image.originalHash = hash != null ? hash : image.original.hash;
			System.out.println("Upload file " + image.original.id + " hash " + image.originalHash);
		}
		if(image.largeHash == null) {
			hash = uploadFile(domain, image.large, "large");
			image.largeHash = hash != null ? hash : image.large.hash;
			System.out.println("Upload file " + image.large.id + " hash " + image.largeHash);
		}
		if(image.mediumHash == null) {
			hash = uploadFile(domain, image.medium, "medium");
			image.mediumHash = hash != null ? hash : image.medium.hash;
			System.out.println("Upload file " + image.medium.id + " hash " + image.mediumHash);
		}
		if(image.smallHash == null) {
			hash = uploadFile(domain, image.small, "small");
			image.smallHash = hash != null ? hash : image.small.hash;
			System.out.println("Upload file " + image.small.id + " hash " + image.smallHash);
		}

		imageRepository.save(image);

		return image;
	}

	private void saveFilesHash(Image image) {
		if(image.original.hash == null) {
			image.original.hash = image.originalHash;
			fileRepository.save(image.original);
		}
		if(image.large.hash == null) {
			image.large.hash = image.largeHash;
			fileRepository.save(image.large);
		}
		if(image.medium.hash == null) {
			image.medium.hash = image.mediumHash;
			fileRepository.save(image.medium);
		}
		if(image.small.hash == null) {
			image.small.hash = image.smallHash;
			fileRepository.save(image.small);
		}
	}

	private void setNetworkIdOnFiles(Integer networkId, Image image) {
		if(image.original.networkId == null) {
			image.original.networkId = networkId;
			fileRepository.save(image.original);
		}
		if(image.large.networkId == null) {
			image.large.networkId = networkId;
			fileRepository.save(image.large);
		}
		if(image.medium.networkId == null) {
			image.medium.networkId = networkId;
			fileRepository.save(image.medium);
		}
		if(image.small.networkId == null) {
			image.small.networkId = networkId;
			fileRepository.save(image.small);
		}
	}

	@Async
	public void uploadAmazonImages() {
			List<Network> networks = networkRepository.findAll();

			System.out.println("Starting upload of files");
			for (Network network : networks) {
				String domain = network.subdomain;

				List<Station> stations = stationRepository.findByNetworkId(network.id);
				for (Station station : stations) {

					System.out.println("Upload of station " + station.id + " of network " + domain);
					List<Post> posts = postRepository.findByStation(station);
					for (Post post : posts) {
						System.out.println("Upload of post " + post.id);
						Image image = post.featuredImage;

						Set<Image> bodyImages = new HashSet<>();
						Matcher m = Pattern.compile("/api/files/\\d+/contents").matcher(post.body);
						while (m.find()) {
							Integer id = Integer.valueOf(m.group().replace("/api/files/", "").replace("/contents", ""));
							bodyImages.add(imageRepository.findByFileId(id));
						}

						if (image != null) {
							try {
								setNetworkIdOnFiles(network.id, image);
								image = uploadImage(image, domain);

								post.imageHash = image.originalHash;
								post.imageLargeHash = image.largeHash;
								post.imageMediumHash = image.mediumHash;
								post.imageSmallHash = image.smallHash;

								postRepository.save(post);
							} catch (SQLException | IOException e) {
								e.printStackTrace();
								continue;
							}
						}

						for (Image img : bodyImages) {
							try {
								if(img != null) {
									setNetworkIdOnFiles(network.id, img);
									uploadImage(img, domain);
								}

							} catch (SQLException | IOException e) {
								e.printStackTrace();
							}
						}
					}
				}

				List<Person> people = personRepository.findAllByNetwork(network.id);
				for (Person person : people) {
					Image cover = person.cover;
					Image profilePicture = person.image;

					if (cover != null) {
						try {
							System.out.println("Upload cover of person " + person.id);
							setNetworkIdOnFiles(network.id, cover);
							cover = uploadImage(cover, domain);

							person.coverMediumHash = cover.mediumHash;
							person.coverLargeHash = cover.largeHash;

							personRepository.save(person);
						} catch (SQLException | IOException e) {
							e.printStackTrace();
						}
					}

					if (profilePicture != null) {
						try {
							System.out.println("Upload profilePicture of person " + person.id);
							setNetworkIdOnFiles(network.id, profilePicture);
							profilePicture = uploadImage(profilePicture, domain);

							person.imageHash = profilePicture.originalHash;
							person.imageLargeHash = profilePicture.largeHash;
							person.imageMediumHash = profilePicture.mediumHash;
							person.imageSmallHash = profilePicture.smallHash;

							personRepository.save(person);
						} catch (SQLException | IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

	}
}
