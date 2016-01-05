package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "network_id"}))
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	public Date createdAt;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

    public String getInvitationUrl(){
        if(network != null && network.domain != null)
            return "http://" + network.domain + "/access/signup?" + "invitation=" + hash;
        return "http://" + network.subdomain + ".trix.rocks/access/signup?" + "invitation=" + hash;
    }

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		invitationUrl = getInvitationUrl();
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
}
