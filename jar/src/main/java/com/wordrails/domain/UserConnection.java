package com.wordrails.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "userconnection")
public class UserConnection implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Column(unique = true)
	public String accessToken;

	public String providerUserId;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public User user;

	@NotNull
	public String providerId;

	public String displayName;

	public String profileUrl;

	public String email;

	public String imageUrl;

	public String secret;

	public String refreshToken;

	public Long expireTime;
}
