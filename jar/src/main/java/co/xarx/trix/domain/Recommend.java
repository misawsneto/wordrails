package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkInclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "person_id"}))
public class Recommend extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@SdkInclude
	@NotNull
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@SdkInclude
	@NotNull
	@ManyToOne
	@JoinColumn(name = "person_id")
	private Person person;
}
