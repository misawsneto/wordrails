package co.xarx.trix.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
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

	public String deviceType;

	public boolean deviceDeactivated;

	@NotEmpty
	@Size(min=1,max=500)
	public String message;

	@NotEmpty
	public String type;

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status.toString();
	}

	public String getErrorCodeName() {
		return errorCodeName;
	}

	public void setErrorCodeName(String errorCodeName) {
		this.errorCodeName = errorCodeName;
	}

	public DeviceType getDeviceType() {
		return DeviceType.valueOf(deviceType);
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType.toString();
	}

	public Boolean getDeviceDeactivated() {
		return deviceDeactivated;
	}

	public void setDeviceDeactivated(Boolean deviceDeactivated) {
		this.deviceDeactivated = deviceDeactivated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
