package com.wordrails.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.wordrails.domain.*;
import com.wordrails.persistence.*;
import com.wordrails.util.FilesUtil;
import com.wordrails.util.TrixUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AmazonCloudService {

	Logger log = Logger.getLogger(AmazonCloudService.class.getName());

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

	private AmazonS3Client s3Client;

	public void init() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		s3Client = new AmazonS3Client(awsCreds);
	}

	public String getPublicImageURL(String networkDomain, String fileName) throws IOException {
		return "http://" + publicCloudfrontUrl + "/" + networkDomain + "/images/" + fileName;
	}

	public String getPublicApkURL(String networkDomain, String fileName) throws IOException {
		return "http://" + publicCloudfrontUrl + "/" + networkDomain + "/apk/" + fileName;
	}

	public String uploadPublicImage(InputStream input, Long lenght, String networkDomain, String size, String mime) throws IOException, AmazonS3Exception {
		java.io.File tmpFile = java.io.File.createTempFile(TrixUtil.generateRandomString(5, "aA#"), ".tmp");

		FileUtils.copyInputStreamToFile(input, tmpFile);
		String hash = FilesUtil.getHash(new FileInputStream(tmpFile));
		uploadPublicImage(tmpFile, lenght, networkDomain, hash, size, mime, true);
		return hash;
	}

	public String uploadAPK(java.io.File file, Long lenght, String networkDomain, String mime, boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
		log.debug("uploading apk...");

		String hash = FilesUtil.getHash(new FileInputStream(file));

		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(mime);

		String path = networkDomain + "/apk/" + hash;

		uploadFile(file, lenght, publicBucket, path, md, deleteFileAfterUpload);

		return hash;
	}


	/**
	 * send hash null if needs to generate it
	 * @param file
	 * @param lenght
	 * @param networkDomain
	 * @param hash
	 * @param sizeTag
	 * @param mime
	 * @param deleteFileAfterUpload
	 * @return
	 * @throws IOException
	 * @throws AmazonS3Exception
	 */
	public String uploadPublicImage(java.io.File file, Long lenght, String networkDomain, String hash,
	                                String sizeTag, String mime, boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
		if(hash == null) {
			hash = FilesUtil.getHash(new FileInputStream(file));
		}

		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(mime);
		md.addUserMetadata("size", sizeTag);

		String path = networkDomain + "/images/" + hash;
		uploadFile(file, lenght, publicBucket, path, md, deleteFileAfterUpload);

		return hash;
	}

	public boolean exists(String bucket, String key) {
		try {
			s3Client.getObjectMetadata(bucket, key);
		} catch(AmazonServiceException e) {
			if (e.getStatusCode() == 403 || e.getStatusCode() == 404) {
				return false;
			} else {
				throw e;
			}
		}
		return true;
	}

	public S3Object get(String bucket, String key) {
		try {
			return s3Client.getObject(bucket, key);
		} catch(AmazonServiceException e) {
			return null;
		}
	}

	private void uploadFile(java.io.File file, Long lenght, String bucketName, String keyName, ObjectMetadata metadata, boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
		log.debug("uploading file of lenght " + lenght);

		if(exists(bucketName, keyName)) {
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
		long partSize = 6 * 1024 * 1024; // Set part size to 7 MB.


		try {
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < lenght; i++) {
				// Last part can be less than 3 MB. Adjust part size.
				partSize = Math.min(partSize, (lenght - filePosition));

				log.debug("uploading file part " + i + ", partSize=" + partSize + ", fileposition=" + filePosition);

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest().
						withBucketName(bucketName).
						withKey(keyName).
						withUploadId(initResponse.getUploadId()).
						withPartNumber(i).
						withFileOffset(filePosition).
						withFile(file).
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
		} finally {
			if(deleteFileAfterUpload && file.exists()) {
				file.delete();
			}
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

	private String uploadFile(String subdomain, File file, String sizeType, Integer networkId) throws SQLException, IOException {
		System.out.println("uploading file " + file.id);

		FileContents fileContents = fileContentsRepository.findOne(file.id);
		InputStream is;
		long byteSize;

		Blob blob = fileContents.contents;
		if(blob == null) {
			if (file.url != null) {
				is = FilesUtil.getStreamFromUrl(file.url);
				byteSize = ((FileInputStream) is).getChannel().size();
			} else if(file.hash != null && !file.hash.isEmpty() && exists(publicBucket, file.hash)) {
				return file.hash;
			} else {
				return null;
			}
		} else {
			is = blob.getBinaryStream();
			byteSize = blob.length();
		}

		if(file.mime == null)
			file.mime = new Tika().detect(is);


		String hash = null;
		try {
			hash = uploadPublicImage(is, byteSize, subdomain, sizeType, file.mime);
			file.hash = hash;
			file.size = byteSize;
			file.type = File.EXTERNAL;
			file.directory = File.DIR_IMAGES;
			file.networkId = networkId;
			fileRepository.save(file);
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		}

		return hash;
	}

	private Image uploadImage(Image image, String subdomain, Integer networkId) throws IOException, SQLException {
		String hash;
		if(image.originalHash == null) {
			hash = uploadFile(subdomain, image.original, "original", networkId);
			image.originalHash = hash != null ? hash : image.original.hash;
			System.out.println("Upload file " + image.original.id + " hash " + image.originalHash);
		}
		if(image.largeHash == null) {
			hash = uploadFile(subdomain, image.large, "large", networkId);
			image.largeHash = hash != null ? hash : image.large.hash;
			System.out.println("Upload file " + image.large.id + " hash " + image.largeHash);
		}
		if(image.mediumHash == null) {
			hash = uploadFile(subdomain, image.medium, "medium", networkId);
			image.mediumHash = hash != null ? hash : image.medium.hash;
			System.out.println("Upload file " + image.medium.id + " hash " + image.mediumHash);
		}
		if(image.smallHash == null) {
			hash = uploadFile(subdomain, image.small, "small", networkId);
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
				String subdomain = network.subdomain;

				List<Station> stations = stationRepository.findByNetworkId(network.id);
				for (Station station : stations) {

					System.out.println("Upload of station " + station.id + " of network " + subdomain);
					List<Post> posts = postRepository.findByStation(station);
					for (Post post : posts) {
						System.out.println("Upload of post " + post.id);
						Image image = post.featuredImage;

						Matcher m = Pattern.compile("/api/files/\\d+/contents").matcher(post.body);
						while (m.find()) {
							Integer id = Integer.valueOf(m.group().replace("/api/files/", "").replace("/contents", ""));
							try {
								uploadFile(subdomain, fileRepository.findOne(id), "original", network.id);
							} catch (SQLException | IOException e) {
								e.printStackTrace();
							}
						}

						if (image != null) {
							try {
								setNetworkIdOnFiles(network.id, image);
								saveFilesHash(image);
								if (image.originalHash == null || image.largeHash == null || image.mediumHash == null || image.smallHash == null) {
									image = uploadImage(image, subdomain, network.id);

									post.imageHash = image.originalHash;
									post.imageLargeHash = image.largeHash;
									post.imageMediumHash = image.mediumHash;
									post.imageSmallHash = image.smallHash;

									postRepository.save(post);
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
							saveFilesHash(cover);
							if (cover.originalHash == null || cover.largeHash == null || cover.mediumHash == null || cover.smallHash == null) {
								cover = uploadImage(cover, subdomain, network.id);

								person.coverMediumHash = cover.mediumHash;
								person.coverLargeHash = cover.largeHash;

								personRepository.save(person);
							}
						} catch (SQLException | IOException e) {
							e.printStackTrace();
						}
					}

					if (profilePicture != null) {
						try {
							System.out.println("Upload profilePicture of person " + person.id);
							setNetworkIdOnFiles(network.id, profilePicture);
							saveFilesHash(profilePicture);
							if (profilePicture.originalHash == null || profilePicture.largeHash == null ||
									profilePicture.mediumHash == null || profilePicture.smallHash == null) {
								profilePicture = uploadImage(profilePicture, subdomain, network.id);

								person.imageHash = profilePicture.originalHash;
								person.imageLargeHash = profilePicture.largeHash;
								person.imageMediumHash = profilePicture.mediumHash;
								person.imageSmallHash = profilePicture.smallHash;

								personRepository.save(person);
							}
						} catch (SQLException | IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

	}
}
