package com.wordrails.services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
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

	//IMAGE DIRECTORY PATTERN: {networkDomain}/images/{fileHash}

	private static final String IMAGE_DIR = "/images/";
	private static final String PUBLIC_BUCKET = "omega.io";
	private static final String PRIVATE_BUCKET = "private";
	private static final String CLOUDFRONT_URL = "d224pxtayz8rv6.cloudfront.net";

	@Value("${amazon.accessKey}")
	String accessKey;
	@Value("${amazon.accessSecretKey}")
	String accessSecretKey;
	@Value("${amazon.distributionKey}")
	String distributionKey;

	private byte[] getBytes(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		org.apache.commons.io.IOUtils.copy(in, baos);
		return baos.toByteArray();
	}

	public String getURL(String networkDomain, String fileName) throws IOException {
		return "http://" + CLOUDFRONT_URL + "/" + networkDomain + IMAGE_DIR + fileName;
	}

	public void uploadPublicImage(InputStream input, Long lenght, String networkDomain, String fileName) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, PUBLIC_BUCKET, fileName);
	}

	public void uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String fileName) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, PRIVATE_BUCKET, fileName);
	}

	public String uploadPublicImage(InputStream input, Long lenght, String networkDomain, String size, String mime) throws IOException, AmazonS3Exception {
		byte[] bytes = getBytes(input);
		String hash = DigestUtils.md5Hex(new ByteArrayInputStream(bytes));
		uploadImage(new ByteArrayInputStream(bytes), lenght, networkDomain, PUBLIC_BUCKET, hash, size, mime);
		return hash;
	}

	public String uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String size, String mime) throws IOException, AmazonS3Exception {
		byte[] bytes = getBytes(input);
		String hash = DigestUtils.md5Hex(new ByteArrayInputStream(bytes));
		uploadImage(new ByteArrayInputStream(bytes), lenght, networkDomain, PRIVATE_BUCKET, hash, size, mime);
		return hash;
	}

	public void uploadPublicImage(InputStream input, Long lenght, String networkDomain, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, PUBLIC_BUCKET, fileName, size, mime);
	}

	public void uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, PRIVATE_BUCKET, fileName, size, mime);
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

	private void uploadFile(InputStream input, Long lenght, String bucketName, String keyName, ObjectMetadata metadata) throws IOException, AmazonS3Exception {
		if (input.available() == 0) {
			throw new AmazonS3Exception("InputStream is empty");
		}

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		AmazonS3 s3Client = new AmazonS3Client(awsCreds);

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

	private String uploadFile(String domain, TrixFile file, String sizeType) throws SQLException, IOException {
		System.out.println("uploading file " + file.id);

		Blob blob = file.contents;
		InputStream is;
		long byteSize = 0;

		if(blob == null) {
			if(file.url != null) {
				is = WordrailsUtil.getStreamFromUrl(file.url);
				byteSize = ((FileInputStream) is).getChannel().size();
			} else {
				return "";
			}
		} else {
			is = blob.getBinaryStream();
			byteSize = blob.length();
		}



		if(file.mime == null) file.mime = new Tika().detect(is);

		String hash = "";

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
		if (image.originalHash != null && image.largeHash != null && image.mediumHash != null && image.smallHash != null) {
			return image;
		}

		if(image.originalHash == null) {
			image.originalHash = uploadFile(domain, image.original, "original");
		}
		if(image.largeHash == null) {
			image.largeHash = uploadFile(domain, image.large, "large");
		}
		if(image.mediumHash == null) {
			image.mediumHash = uploadFile(domain, image.medium, "medium");
		}
		if(image.smallHash == null) {
			image.smallHash = uploadFile(domain, image.small, "small");
		}

		imageRepository.save(image);

		saveFilesHash(image);
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

						if(image != null) {
							if (post.imageHash == null || post.imageLargeHash == null || post.imageMediumHash == null || post.imageSmallHash == null
									|| image.originalHash == null || image.largeHash == null || image.mediumHash == null || image.smallHash == null) {
								try {
									System.out.println("Upload featureImage of post " + post.id);
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
							} else {
								saveFilesHash(image);
							}
						}

						for (Image img : bodyImages) {
							try {
								if(img != null) {
									System.out.println("Upload body images of post " + post.id);
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
					System.out.println("Upload of person " + person.id);
					Image cover = person.cover;
					Image profilePicture = person.image;

					if(cover != null) {
						if (person.coverMediumHash == null || person.coverLargeHash == null || cover.mediumHash == null || cover.largeHash == null) {
							try {
								System.out.println("Upload cover of person " + person.id);
								cover = uploadImage(cover, domain);

								person.coverMediumHash = cover.mediumHash;
								person.coverLargeHash = cover.largeHash;

								personRepository.save(person);
							} catch (SQLException | IOException e) {
								e.printStackTrace();
							}
						} else {
							saveFilesHash(cover);
						}
					}

					if(profilePicture != null) {
						if (person.imageHash == null || person.imageLargeHash == null ||
								person.imageMediumHash == null || person.imageSmallHash == null ||
								profilePicture.originalHash == null || profilePicture.largeHash == null ||
								profilePicture.mediumHash == null || profilePicture.smallHash == null) {
							try {
								System.out.println("Upload profilePicture of person " + person.id);
								profilePicture = uploadImage(profilePicture, domain);

								person.imageHash = profilePicture.originalHash;
								person.imageLargeHash = profilePicture.largeHash;
								person.imageMediumHash = profilePicture.mediumHash;
								person.imageSmallHash = profilePicture.smallHash;

								personRepository.save(person);
							} catch (SQLException | IOException e) {
								e.printStackTrace();
							}
						} else {
							saveFilesHash(profilePicture);
						}
					}
				}
			}

	}
}
