package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class MobileNotification extends Notification {

	public enum DeviceType {
		ANDROID,
		APPLE
	}

	public enum Status {
		SEND_ERROR,
		SERVER_ERROR,
		SUCCESS
	}

	public MobileNotification(String regId, String hash, Status status, String message, NotificationType type) {
		this.regId = regId;
		this.hash = hash;
		this.status = status.toString();
		this.message = message;
		this.type = type;
	}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String regId;

	@NotNull
	public String hash;

	@Column(name = "post_id")
	public Integer postId;

	public boolean test = false;

	@NotEmpty
	public String status;

	public String errorCodeName;

	@Lob
	public String stackTrace;

	public String deviceType;

	public boolean deviceDeactivated;

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType.toString();
	}
}
