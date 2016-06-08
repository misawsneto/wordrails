package co.xarx.trix.domain.social;

import lombok.Data;

@Data
public class GoogleUser implements SocialUser {

	private String providerId;
	private String id;
	private String name;
	private String email;
	private String coverUrl;
	private String profileUrl;
	private String profileImageUrl;

	@Override
	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
}
