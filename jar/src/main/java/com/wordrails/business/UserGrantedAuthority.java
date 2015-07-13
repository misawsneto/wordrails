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

	public UserGrantedAuthority(User user, String authority, Network network) {
		this.user = user;
		this.authority = authority;
		this.network = network;
	}

	public UserGrantedAuthority(User user, String authority, Network network, Station station) {
		this.user = user;
		this.authority = authority;
		this.network = network;
		this.station = station;
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
