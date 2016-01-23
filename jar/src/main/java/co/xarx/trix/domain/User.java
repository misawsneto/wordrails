package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used by spring-security for authorizing users based on username and password.
 * For a functional representation of a user object for this system, check the {@link Person} class.
 *
 * @author misael
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "networkId"}))
public class User extends BaseEntity implements UserDetails, Serializable {

	private static final long serialVersionUID = -4656215770382382924L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@Size(max = 50)
	public String username;

	@Size(max = 500)
	public String password;

	public Boolean enabled;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.ALL})
	public Set<UserGrantedAuthority> authorities;

	@JsonIgnore
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
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}