package com.wordrails.business;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="authorities")
public class UserGrantedAuthority implements GrantedAuthority {

	public UserGrantedAuthority() {
	}

	public UserGrantedAuthority(String authority) {
		this.authority = authority;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@ManyToOne
	@JoinColumn(name="user_id")
	public User user;

	@NotNull
	public String authority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="network_id")
	public Network network;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="station_id")
	public Station station;

	@Override
	public String getAuthority() {
		return authority;
	}
}
