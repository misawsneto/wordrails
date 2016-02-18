package co.xarx.trix.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Ad extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@OneToOne
	public Image image;

	@Lob
	public String link;

	@NotNull
	@ManyToOne
	Sponsor sponsor;
}
