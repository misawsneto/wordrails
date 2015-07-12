package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "userconnection")
public class UserConnection {

	public enum Provider {
		FACEBOOK("facebook"),
		GOOGLE("google"),
		TWITTER("twitter");

		private final String text;

		Provider(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Column(unique = true)
	public String accessToken;

	public String userId;

	public String providerUserId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	@NotNull
	public Provider provider;

	public String displayName;

	public String profileUrl;

	public String imageUrl;

	public String secret;

	public String refreshToken;

	public Long expireTime;
}
