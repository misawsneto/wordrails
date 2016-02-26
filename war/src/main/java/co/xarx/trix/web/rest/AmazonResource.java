package co.xarx.trix.web.rest;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Path("/amazon")
@Component
public class AmazonResource {

	@Value("${trix.amazon.key}")
	String accessKey;
	@Value("${trix.amazon.secret}")
	String accessSecretKey;
	@Value("${trix.amazon.bucket}")
	String publicBucket;

	private AmazonS3 s3() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, accessSecretKey);
		return new AmazonS3Client(awsCreds);
	}

	@GET
	@Path("/signedUrl")
	public Response generateSignedUrl(@QueryParam("hash") String hash,
									  @QueryParam("type") String type) throws IOException {
		String objectKey = TenantContextHolder.getCurrentTenantId() + "/" + type + "/" + hash;

		System.out.println("Generating pre-signed URL.");
		java.util.Date expiration = new java.util.Date();
		long milliSeconds = expiration.getTime();
		milliSeconds += 1000 * 60 * 5; // 5 minutes
		expiration.setTime(milliSeconds);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(publicBucket, objectKey);
		generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
		generatePresignedUrlRequest.setExpiration(expiration);

		AmazonS3 amazonS3 = s3();
		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		String signedUrl = url.toString();

		System.out.println("Pre-Signed URL = " + signedUrl);

		return Response.ok().entity(signedUrl).build();
	}

	@POST
	@Path("/testSignedUrl")
	public Response UploadObject(@Context HttpServletRequest request) throws IOException {
//		String url = (String) this.generateSignedUrl("teste", "videos", request).getEntity();
		String url = "https://public-server-test.s3.amazonaws.com/demo/videos/af9317eacdd00d88f72279004f57ab2d?AWSAccessKeyId=AKIAJ2L7I36ADEIBW6FQ&Expires=1444419499&Signature=9qLgUUCHdF13PdbR8b3KSARlxL4%3D";

		HttpURLConnection connection=(HttpURLConnection) new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("PUT");
		OutputStreamWriter out = new OutputStreamWriter(
				connection.getOutputStream());
		out.write("This text uploaded as object.");
		out.close();
		int responseCode = connection.getResponseCode();
		System.out.println("Service returned response code " + responseCode);

		return Response.ok().build();
	}
}
