package co.xarx.trix.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="authorities")
public class UserGrantedAuthority extends BaseEntity implements GrantedAuthority {

	public static final String USER = "ROLE_USER";
	public static final String NETWORK_ADMIN = "ROLE_NETWORK_ADMIN";
	public static final String STATION_ADMIN = "ROLE_STATION_ADMIN";
	public static final String STATION_EDITOR = "ROLE_STATION_EDITOR";
	public static final String STATION_WRITER = "ROLE_STATION_WRITER";

	public UserGrantedAuthority() {
		this(null, USER);
	}

	public UserGrantedAuthority(User user, String authority) {
		this.user = user;
		this.authority = authority;
	}

	public UserGrantedAuthority(User user, String authority, Station station) {
		this.user = user;
		this.authority = authority;
		this.station = station;
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

	@ManyToOne
	public Station station;

	@Override
	public String getAuthority() {
		String stationString = "";
		if(station != null) stationString = "_" + station.id;
		return authority + stationString;
	}
}
