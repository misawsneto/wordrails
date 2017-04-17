package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationData {
	private Integer id;
	private String tenantId;
	private Integer stationId;
	private String stationName;
	private String hash;
	private String message;
	private String imageUrl;
	private Integer entityId;
	private String entityType;
	private Boolean test = false;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	Date createdAt;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	Date updatedAt;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date scheduledDate;
	private String title;
	private String geo;
}