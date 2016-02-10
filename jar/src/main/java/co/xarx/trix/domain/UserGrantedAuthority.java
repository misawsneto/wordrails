package co.xarx.trix.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="authorities")
public class UserGrantedAuthority extends BaseEntity implements GrantedAuthority {

	public static final String USER = "ROLE_USER";
	public static final String NETWORK_ADMIN = "ROLE_NETWORK_ADMIN";

	protected UserGrantedAuthority() {
		this(null, USER);
	}

	public UserGrantedAuthority(User user) {
		this(user, USER);
	}

	public UserGrantedAuthority(User user, String authority) {
		this.user = user;
		this.authority = authority;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@ManyToOne
	public User user;

	@NotNull
	public String authority;

	@Override
	public String getAuthority() {
		return authority;
	}
}
