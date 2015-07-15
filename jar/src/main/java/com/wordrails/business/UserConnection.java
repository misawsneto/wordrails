package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "userconnection")
public class UserConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Column(unique = true)
	public String accessToken;

	public String providerUserId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	@NotNull
	public String providerId;

	public String displayName;

	public String profileUrl;

	public String imageUrl;

	public String secret;

	public String refreshToken;

	public Long expireTime;
}
