package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "person_id", "sessionid"}))
public class PostRead extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@ManyToOne
	public Person person;

	@ManyToOne
	public Post post;

	public String sessionid;
}
