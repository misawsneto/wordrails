package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@lombok.Getter
@lombok.Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "network_id"}))
public class Invitation extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@Column(unique = true)
	public String hash;

	@Email
	public String email;

	public String personName;

	public String invitationUrl;

	public boolean active = true;

	@ManyToOne
	@JoinColumn(name = "station_id")
	public Station station;

	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		invitationUrl = getUrl();
	}

	public String getUrl() {
		return "http://" + network.getTenantId() + ".trix.rocks/" + "invitation?hash=" + hash;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
}
