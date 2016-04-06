package co.xarx.trix.web.rest.resource;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.AmazonApi;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.util.Assert;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;

@Slf4j
@Component
public class AmazonResource extends AbstractResource implements AmazonApi {

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

	@Override
	public Response generateSignedUrl(String hash, String type) throws IOException {
		Assert.hasText(hash, "Hash must not be empty");
		Assert.hasText(type, "Type must not be empty");

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

		log.debug("Pre-Signed URL = " + signedUrl);

		return Response.ok().entity(signedUrl).build();
	}
}
