package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.util.FileUtil;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import co.xarx.trix.util.TrixUtil;
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

	public static final String APK_DIR = "apk";
	public static final String IMAGE_DIR = "images";

	Logger log = Logger.getLogger(AmazonCloudService.class.getName());

	@Value("${amazon.accessKey}")
	String accessKey;
	@Value("${amazon.accessSecretKey}")
	String accessSecretKey;
	@Value("${amazon.distributionKey}")
	String distributionKey;
	@Value("${amazon.publicCloudfrontUrl}")
	String publicCloudfrontUrl;
	@Value("${amazon.publicBucket}")
	String publicBucket;

	private AmazonS3Client s3Client;

	public void init() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		s3Client = new AmazonS3Client(awsCreds);
	}

	public String getPublicImageURL(String fileName) throws IOException {
		String networkDomain = TenantContextHolder.getCurrentTenantSubdomain();
		return "http://" + publicCloudfrontUrl + "/" + networkDomain + "/images/" + fileName;
	}

	public String getPublicApkURL(String fileName) throws IOException {
		String networkDomain = TenantContextHolder.getCurrentTenantSubdomain();
		return "http://" + publicCloudfrontUrl + "/" + networkDomain + "/apk/" + fileName;
	}

	public String uploadPublicImage(InputStream input, Long lenght, String size, String mime) throws IOException, AmazonS3Exception {
		java.io.File tmpFile = java.io.File.createTempFile(TrixUtil.generateRandomString(5, "aA#"), ".tmp");

		FileUtils.copyInputStreamToFile(input, tmpFile);
		String hash = FileUtil.getHash(new FileInputStream(tmpFile));
		uploadPublicImage(tmpFile, lenght, hash, size, mime, true);
		return hash;
	}

	public String uploadAPK(java.io.File file, Long lenght, String mime, boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
		log.debug("uploading apk...");

		String hash = FileUtil.getHash(new FileInputStream(file));

		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(mime);

		String path = getKey(APK_DIR, hash);

		uploadFile(file, lenght, path, md, deleteFileAfterUpload);

		return hash;
	}


	/**
	 * send hash null if needs to generate it
	 */
	public String uploadPublicImage(java.io.File file, Long lenght, String hash,
	                                String sizeTag, String mime, boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
		if(hash == null) {
			hash = FileUtil.getHash(new FileInputStream(file));
		}

		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(mime);
		md.addUserMetadata("size", sizeTag);

		String path = getKey(IMAGE_DIR, hash);
		uploadFile(file, lenght, path, md, deleteFileAfterUpload);

		return hash;
	}

	private String getKey(String directory, String hash) {
		return TenantContextHolder.getCurrentTenantSubdomain() + "/" + directory + "/" + hash;
	}

	public boolean exists(String directory, String hash) {
		return exists(getKey(directory, hash));
	}

	public boolean exists(String key) {
		try {
			s3Client.getObjectMetadata(publicBucket, key);
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

	private void uploadFile(java.io.File file, Long lenght, String keyName, ObjectMetadata metadata, boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
		log.debug("uploading file of lenght " + lenght);

		if(exists(keyName)) {
			return;
		}

		// Create a list of UploadPartResponse objects. You get one of these for
		// each part upload.
		List<PartETag> partETags = new ArrayList<>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(publicBucket, keyName);
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
						withBucketName(publicBucket).
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
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(publicBucket, keyName, initResponse.getUploadId(), partETags);

			s3Client.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(publicBucket, keyName, initResponse.getUploadId()));

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

	private String uploadFile(File file, String sizeType) throws SQLException, IOException {
		System.out.println("uploading file " + file.id);

		FileContents fileContents = fileContentsRepository.findOne(file.id);
		InputStream is;
		long byteSize;

		Blob blob = fileContents.contents;
		if(blob == null) {
			if (file.url != null) {
				is = FileUtil.getStreamFromUrl(file.url);
				byteSize = ((FileInputStream) is).getChannel().size();
			} else if(file.hash != null && !file.hash.isEmpty() && exists(getKey(IMAGE_DIR, file.hash))) {
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
			hash = uploadPublicImage(is, byteSize, sizeType, file.mime);
			file.hash = hash;
			file.size = byteSize;
			file.type = File.EXTERNAL;
			file.directory = File.DIR_IMAGES;
			fileRepository.save(file);
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		}

		return hash;
	}

	private Image uploadImage(Image image) throws IOException, SQLException {
		String hash;
		if(image.originalHash == null) {
			hash = uploadFile(image.original, "original");
			image.originalHash = hash != null ? hash : image.original.hash;
			System.out.println("Upload file " + image.original.id + " hash " + image.originalHash);
		}
		if(image.largeHash == null) {
			hash = uploadFile(image.large, "large");
			image.largeHash = hash != null ? hash : image.large.hash;
			System.out.println("Upload file " + image.large.id + " hash " + image.largeHash);
		}
		if(image.mediumHash == null) {
			hash = uploadFile(image.medium, "medium");
			image.mediumHash = hash != null ? hash : image.medium.hash;
			System.out.println("Upload file " + image.medium.id + " hash " + image.mediumHash);
		}
		if(image.smallHash == null) {
			hash = uploadFile(image.small, "small");
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
								uploadFile(fileRepository.findOne(id), "original");
							} catch (SQLException | IOException e) {
								e.printStackTrace();
							}
						}

						if (image != null) {
							try {
								saveFilesHash(image);
								if (image.originalHash == null || image.largeHash == null || image.mediumHash == null || image.smallHash == null) {
									image = uploadImage(image);

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

				List<Person> people = personRepository.findAll();
				for (Person person : people) {
					Image cover = person.cover;
					Image profilePicture = person.image;

					if (cover != null) {
						try {
							System.out.println("Upload cover of person " + person.id);
							saveFilesHash(cover);
							if (cover.originalHash == null || cover.largeHash == null || cover.mediumHash == null || cover.smallHash == null) {
								cover = uploadImage(cover);

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
							saveFilesHash(profilePicture);
							if (profilePicture.originalHash == null || profilePicture.largeHash == null ||
									profilePicture.mediumHash == null || profilePicture.smallHash == null) {
								profilePicture = uploadImage(profilePicture);

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
