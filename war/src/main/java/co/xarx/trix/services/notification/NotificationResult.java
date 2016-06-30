package co.xarx.trix.services.notification;

import co.xarx.trix.domain.MobileNotification;

public class NotificationResult {

	private MobileNotification.Status status;
	private String errorMessage;
	private boolean deviceDeactivated;

	public MobileNotification.Status getStatus() {
		return status;
	}

	public void setStatus(MobileNotification.Status status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isDeviceDeactivated() {
		return deviceDeactivated;
	}

	public void setDeviceDeactivated(boolean deviceDeactivated) {
		this.deviceDeactivated = deviceDeactivated;
	}
}
