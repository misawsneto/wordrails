package com.wordrails.resource;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;

@Path("/amazon")
@Component
public class AmazonResource {

	@Value("${amazon.accessKey}")
	String accessKey;
	@Value("${amazon.accessSecretKey}")
	String accessSecretKey;
	@Value("${amazon.publicBucket}")
	String publicBucket;

	private AmazonS3 s3() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		return new AmazonS3Client(awsCreds);
	}

	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/signedUrl")
	public Response generateSignedUrl(@FormParam("objectKey") String objectKey) throws IOException {
		System.out.println("Generating pre-signed URL.");
		java.util.Date expiration = new java.util.Date();
		long milliSeconds = expiration.getTime();
		milliSeconds += 1000 * 60 * 5; // 5 minutes
		expiration.setTime(milliSeconds);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(publicBucket, objectKey);
		generatePresignedUrlRequest.setMethod(HttpMethod.GET);
		generatePresignedUrlRequest.setExpiration(expiration);

		URL url = s3().generatePresignedUrl(generatePresignedUrlRequest);
		String signedUrl = url.toString();

		System.out.println("Pre-Signed URL = " + url.toString());

		return Response.ok().entity("{\"signedUrl\":" + signedUrl + "}").build();
	}
}
