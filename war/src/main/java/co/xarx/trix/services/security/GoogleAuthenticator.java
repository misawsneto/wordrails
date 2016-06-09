package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.social.GoogleUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.utils.MapUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@IntegrationTestBean
public class GoogleAuthenticator implements OAuthAuthenticator {

	@Override
	public boolean login(String userId, String appId, String appSecret, String accessToken) {
		try {
			Response response = oauthRequest(userId, accessToken);
			return response.isSuccessful();
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public GoogleUser oauth(String userId, String appId, String appSecret, String accessToken) throws IOException {
		Response response = oauthRequest(userId, accessToken);

		if (!response.isSuccessful())
			throw new IOException("ERROR: " + response.getCode() + " - " + response.getMessage());

		GoogleUser googleUser = new GoogleUser();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(response.getBody());

		googleUser.setId(getId(rootNode));
		googleUser.setName(getName(rootNode));
		googleUser.setCoverUrl(getCoverUrl(rootNode));
		googleUser.setEmail(getEmail(rootNode));
		googleUser.setProviderId("google");
		googleUser.setProfileUrl("http://plus.google.com/" + googleUser.getId());
		googleUser.setProfileImageUrl(getImageUrl(rootNode));

		return googleUser;
	}

	private Response oauthRequest(String userId, String accessToken) throws IOException {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/" + userId + "?fields=cover/coverPhoto/url,emails,id,name,image");
		request.addHeader("Authorization", "Bearer " + accessToken);
		System.out.println(MapUtils.toString(request.getOauthParameters()));
		System.out.println(MapUtils.toString(request.getHeaders()));
		Response response = request.send();
		System.out.println(response.getBody());

		return response;
	}

	private String getImageUrl(JsonNode rootNode) {
		String imageUrl = null;
		try {
			JsonNode image = rootNode.get("image");
			imageUrl = image.get("url").asText();
			//image comes in size 50px. replace the url to show the default sized image
			if (imageUrl.contains("?sz=50")) {
				imageUrl = imageUrl.replace("?sz=50", "");
			}
		} catch (Exception ignored) {
		}
		return imageUrl;
	}

	private String getEmail(JsonNode rootNode) throws IOException {
		String email = null;
		try {
			JsonNode emails = rootNode.get("emails");
			for (JsonNode emailNode : emails) {
				email = emailNode.get("value").asText();
				if (emailNode.get("type").asText().equals("account")) {
					break;
				}
			}
		} catch (Exception e) {
			throw new IOException("Request is incorrect, email is not present in the response");
		}
		return email;
	}

	private String getCoverUrl(JsonNode rootNode) {
		String cover = null;
		try {
			cover = rootNode.get("cover").get("coverPhoto").get("url").asText();
		} catch (Exception ignored) {
		}
		return cover;
	}

	private String getName(JsonNode rootNode) throws IOException {
		String name;
		try {
			JsonNode node = rootNode.get("node");
			name = node.get("givenName").asText() + " " + node.get("familyName").asText();
		} catch (Exception e) {
			throw new IOException("Request is incorrect, name is not present in the response");
		}
		return name;
	}

	private String getId(JsonNode rootNode) throws IOException {
		String id;
		try {
			id = rootNode.get("id").asText();
		} catch (Exception e) {
			throw new IOException("Request is incorrect, ID is not present in the response");
		}
		return id;
	}
}
