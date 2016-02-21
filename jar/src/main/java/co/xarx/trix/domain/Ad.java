package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
public class Ad extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@OneToOne
	public Image image;

	@Lob
	public String link;

	@NotNull
	@ManyToOne
	Sponsor sponsor;
}
