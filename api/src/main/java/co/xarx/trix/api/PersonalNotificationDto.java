package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by misael on 4/16/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalNotificationDto {
	private Integer id;
	private Integer notificationRequestId;
	private Integer mobileDeviceId;
	private String returnMessageId;
	private String deviceCode;
	private String deviceType;
	private String status;
	private String errorCodeName;
	private Boolean seen;
	private Boolean test;
	private Boolean deactivateDevice;
	private String stacktrace;
}
