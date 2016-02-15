package co.xarx.trix.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

	public enum Type {
		ADDED_TO_STATION,
		REMOVED_FROM_STATION,
		POST_COMMENTED,
		POST_DELETED,
		POST_ADDED,
		BREAKING_NEWS,
		MESSAGE,
		IREPORT_INVITE,
		IREPORT_REVOKE
	}

	public enum DeviceType {
		ANDROID,
		APPLE
	}

	public enum Status {
		SEND_ERROR,
		SERVER_ERROR,
		SUCCESS
	}

	protected Notification() {
	}

	public Notification(String regId, String hash, Status status, String message, String type) {
		this.regId = regId;
		this.hash = hash;
		this.status = status.toString();
		this.message = message;
		this.type = type;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	public String regId;

	@NotNull
	public String hash;
	
	@ManyToOne
	public Post post;

	public boolean test = false;

	@NotEmpty
	public String status;

	public String errorCodeName;

	@Lob
	public String stackTrace;

	public String deviceType;

	public boolean deviceDeactivated;

	@NotEmpty
	@Size(min=1,max=500)
	public String message;

	@NotEmpty
	public String type;

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType.toString();
	}
}
