package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
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
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

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
