package co.xarx.trix.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="authorities")
public class UserGrantedAuthority implements GrantedAuthority {

	public static final String USER = "ROLE_USER";
	public static final String NETWORK_ADMIN = "ROLE_NETWORK_ADMIN";
	public static final String STATION_ADMIN = "ROLE_STATION_ADMIN";
	public static final String STATION_EDITOR = "ROLE_STATION_EDITOR";
	public static final String STATION_WRITER = "ROLE_STATION_WRITER";

	public UserGrantedAuthority() {
		this(USER);
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