package com.wordrails.business;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class is used by spring-security for authorizing users based on username and password.
 * For a functional representation of a user object for this system, check the {@link Person} class.  
 * @author misael
 */
@Entity
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames={"username", "networkId"}))
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@Size(max=50)
	public String username;
	
	@NotNull
	@Size(max=500)	
	public String password;
	
	@NotNull
	public boolean enabled;

	@NotNull
	public Integer networkId;

	@OneToOne(mappedBy = "user")
	public Person person;

	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(
		name="authorities",
		joinColumns=@JoinColumn(name="id")
	)
	@Column(name="authority", length=50, nullable=true)
	public Set<String> authorities;

	@Transient
	private Collection<GrantedAuthority> grantedAuthorities;

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		if(grantedAuthorities == null) {
			grantedAuthorities = new HashSet<>();

			authorities.size(); //to fetch the collection from db

			for(String a : authorities) {
				grantedAuthorities.add(new SimpleGrantedAuthority(a));
			}
		}

		return grantedAuthorities;
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