package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.StringUtil;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
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
		java.io.File tmpFile = java.io.File.createTempFile(StringUtil.generateRandomString(5, "aA#"), ".tmp");

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
}
