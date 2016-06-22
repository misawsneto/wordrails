package co.xarx.trix.domain.social;

import lombok.Data;

@Data
public class FacebookUser implements SocialUser {

	private String providerId;

	private String id;
	private String name;
	private String email;
	private Cover cover;
	private String profileUrl;
	private String profileImageUrl;

	@Override
	public String getCoverUrl() {
		return cover.getSource();
	}

	public static class Cover {
		private String id;
		private String source;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}
}
