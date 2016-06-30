package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NotificationRequest extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	private String hash;

	@NotNull
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@NotEmpty
	@Size(min=1,max=500)
	private String message;

	@NotEmpty
	private String title;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "notificationrequest_person", joinColumns = @JoinColumn(name = "notification_id"))
	private List<Integer> personsIds;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "notificationrequest_station", joinColumns = @JoinColumn(name = "notification_id"))
	private List<Integer> stationsIds;

	private Integer postId;

	private Date scheduledDate;
}
