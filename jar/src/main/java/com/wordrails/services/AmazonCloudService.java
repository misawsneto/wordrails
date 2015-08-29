package com.wordrails.services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmazonCloudService {

	//IMAGE DIRECTORY PATTERN: {networkDomain}/images/{fileName}

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

	public String getURL(String networkDomain, String fileName) throws IOException {
		return "http://" + CLOUDFRONT_URL + "/" + networkDomain + IMAGE_DIR + fileName;
	}

	public void uploadPublicImage(InputStream input, Long lenght, String networkDomain, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, "omega.io", fileName, size, mime);
	}

	public void uploadPrivateImage(InputStream input, Long lenght, String networkDomain, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		uploadImage(input, lenght, networkDomain, "private", fileName, size, mime);
	}

	private void uploadImage(InputStream input, Long lenght, String networkDomain, String bucketName, String fileName, String size, String mime) throws IOException, AmazonS3Exception {
		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(mime);
		md.addUserMetadata("size", size);

		String path = networkDomain + "/images/" + fileName;
		uploadFile(input, lenght, bucketName, path, md);
	}

	private void uploadFile(InputStream input, Long lenght, String bucketName, String keyName, ObjectMetadata metadata) throws IOException, AmazonS3Exception {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		AmazonS3 s3Client = new AmazonS3Client(awsCreds);

		// Create a list of UploadPartResponse objects. You get one of these for
		// each part upload.
		List<PartETag> partETags = new ArrayList<>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName, metadata);
		initRequest.setCannedACL(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		long partSize = 3 * 1024 * 1024; // Set part size to 3 MB.

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
}
