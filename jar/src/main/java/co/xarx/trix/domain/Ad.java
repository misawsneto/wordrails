package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class Ad extends BaseEntity {

	@OneToOne
	public Image image;

	@Lob
	public String link;

	@NotNull
	@ManyToOne
	Sponsor sponsor;
}
