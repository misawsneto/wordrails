package com.wordrails.resource;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import com.wordrails.util.WordrailsUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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

	@Autowired
	private WordrailsService wordrailsService;

	private AmazonS3 s3() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		return new AmazonS3Client(awsCreds);
	}

	@GET
	@Path("/signedUrl")
	public Response generateSignedUrl(@QueryParam("hash") String hash, @QueryParam("type") String type, @Context HttpServletRequest request) throws IOException {
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		String objectKey = network.subdomain + "/" + type + "/" + hash;

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

		System.out.println("Pre-Signed URL = " + signedUrl);

		return Response.ok().entity(signedUrl).build();
	}
}
