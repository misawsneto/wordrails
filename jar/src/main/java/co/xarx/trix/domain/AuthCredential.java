package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rometools.utils.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"tenantId"}),
		@UniqueConstraint(columnNames = {"network_id"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
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

	@OneToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	public Network network;

	public boolean isFacebookLoginAllowed() {
		return Strings.isNotEmpty(facebookAppID) && Strings.isNotEmpty(facebookAppSecret);
	}

	public boolean isGoogleLoginAllowed() {
		return Strings.isNotEmpty(googleAppID) && Strings.isNotEmpty(googleAppSecret);
	}

	@PreUpdate
	public void preUpdate(){
		System.out.println("FUCK ME IN THE ASS!!");
	}
}
