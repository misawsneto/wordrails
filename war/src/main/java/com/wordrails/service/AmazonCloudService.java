package com.wordrails.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmazonCloudService {

	@Value("${amazon.accessKey}")
	String accessKey;
	@Value("${amazon.accessSecretKey}")
	String accessSecretKey;

	public String uploadImage(File file, String bucketName, String keyName) throws IOException, AmazonS3Exception {
		ObjectMetadata md = new ObjectMetadata();
		md.setContentType("image/" + FilenameUtils.getExtension(file.getName()));

		return uploadFile(file, bucketName, keyName, md);
	}

	public String uploadFile(File file, String bucketName, String keyName, ObjectMetadata metadata) throws IOException, AmazonS3Exception {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		AmazonS3 s3Client = new AmazonS3Client(awsCreds);

		// Create a list of UploadPartResponse objects. You get one of these for
		// each part upload.
		List<PartETag> partETags = new ArrayList<>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		long contentLength = file.length();
		long partSize = 3 * 1024 * 1024; // Set part size to 3 MB.

		try {
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 3 MB. Adjust part size.
				partSize = Math.min(partSize, (contentLength - filePosition));

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest().
						withBucketName(bucketName).
						withKey(keyName).
						withUploadId(initResponse.getUploadId()).
						withPartNumber(i).
						withFileOffset(filePosition).
						withFile(file).
						withPartSize(partSize).
						withObjectMetadata(metadata);

				// Upload part and add response to our list.
				partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId(), partETags);

			CompleteMultipartUploadResult completeMultipartUploadResult = s3Client.completeMultipartUpload(compRequest);

			return completeMultipartUploadResult.getLocation();
		} catch (Exception e) {
			s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));

			throw new AmazonS3Exception("Error uploading file to Amazon S3");
		}
	}
}
