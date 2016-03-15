package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@SdkExclude
@lombok.Getter
@lombok.Setter
@Entity
@Table(name="authorities")
public class UserGrantedAuthority extends BaseEntity implements GrantedAuthority {

	private static final long serialVersionUID = 4537641657885418480L;

	public static final String USER = "ROLE_USER";
	public static final String ADMIN = "ROLE_ADMIN";

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
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@JsonIgnore
	@ManyToOne
	public User user;

	@NotNull
	public String authority;

	@Override
	public String getAuthority() {
		return authority;
	}
}
