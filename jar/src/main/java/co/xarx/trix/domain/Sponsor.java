package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Sponsor extends BaseEntity {


	@NotNull
	@ManyToOne
	public Network network;

	@Size(min = 1, max = 200)
	public String name;

	@Size(max = 1000)
	public String keywords;

	@Lob
	public String link;

	@OneToOne
	public Image logo;

	@OneToMany
	public Set<Ad> ads;
}