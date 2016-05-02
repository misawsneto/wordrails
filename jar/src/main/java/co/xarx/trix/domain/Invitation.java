package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@lombok.Getter
@lombok.Setter
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "network_id"}))
public class Invitation extends BaseEntity {

	public Invitation(String baseUrl, boolean sendPlainPassword){
		this.hash = UUID.randomUUID().toString();
		this.invitationUrl = "http://" + baseUrl + "/invitation?hash=" + hash;
		this.sendPlainPassword = sendPlainPassword;
	}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@Column(unique = true)
	public String hash;

	@Transient
	public String invitationUrl;

	@Transient
	public boolean sendPlainPassword;

	@OneToOne
	@JoinColumn(name = "person_id")
	public Person person;

	@Lob
	public String email;

	public boolean multipleUser = false;

	public boolean active = true;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "invitation_station", joinColumns = @JoinColumn(name = "invitation_id"))
	@Column(name = "station_id")
	public List<Integer> invitationStations;

	@PrePersist
	void onCreate() {
		setCreatedAt(new Date());
	}

	@PreUpdate
	void onUpdate() {
		setUpdatedAt(new Date());
	}
}
