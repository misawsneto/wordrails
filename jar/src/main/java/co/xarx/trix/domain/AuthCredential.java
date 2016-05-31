package co.xarx.trix.domain;

import com.rometools.utils.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId"}))
public class AuthCredential extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	public String facebookAppSecret;
	public String facebookAppID;

	public String googleAppSecret;
	public String googleAppID;

	@OneToOne
	@NotNull
	public Network network;

	public boolean isFacebookLoginAllowed() {
		return Strings.isNotEmpty(facebookAppID) && Strings.isNotEmpty(facebookAppSecret);
	}

	public boolean isGoogleLoginAllowed() {
		return Strings.isNotEmpty(googleAppID) && Strings.isNotEmpty(googleAppSecret);
	}
}
