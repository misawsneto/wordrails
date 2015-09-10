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
	public User user;

	@NotNull
	public String authority;

	@ManyToOne
	public Network network;

	@ManyToOne
	public Station station;

	@Override
	public String getAuthority() {
		return authority;
	}
}
