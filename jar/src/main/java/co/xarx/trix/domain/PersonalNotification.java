package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PersonalNotification extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private NotificationRequest request;

	@NotEmpty
	private String message;

	@NotEmpty
	private String title;

	@ManyToOne
	private Person person;

	private Date sentAt;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "personalnotification_properties", joinColumns = @JoinColumn(name = "notification_id"))
	@MapKeyColumn(name = "name", nullable = false, length = 100)
	@Column(name = "value", nullable = false)
	private Map<String, String> properties;
}
