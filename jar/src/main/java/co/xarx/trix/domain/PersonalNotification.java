package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PersonalNotification extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@ManyToOne
	private NotificationRequest request;

	@NotEmpty
	private String message;

	@NotEmpty
	private String title;

	@ManyToOne
	private Person person;

	private Date sentAt;

	private Integer postId;
}
