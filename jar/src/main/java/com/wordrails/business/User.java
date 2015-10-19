package com.wordrails.business;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used by spring-security for authorizing users based on username and password.
 * For a functional representation of a user object for this system, check the {@link Person} class.
 *
 * @author misael
 */
@Entity
@Table(name = "users")//, uniqueConstraints = @UniqueConstraint(columnNames = {"username", "network_id"}))
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Size(max = 50)
	public String username;

	@Size(max = 500)
	public String password;

	public boolean enabled;

	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;

	@OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
	public Person person;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.ALL})
	public Set<UserGrantedAuthority> authorities;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.ALL})
	public Set<UserConnection> userConnections;

	public void addAuthority(UserGrantedAuthority authority) {
		if (authorities == null) authorities = new HashSet<>();

		authorities.add(authority);
	}

	@Override
	public Set<UserGrantedAuthority> getAuthorities() {
		return authorities;
	}

	public boolean isAnonymous() {
		return username.equals("wordrails");
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}