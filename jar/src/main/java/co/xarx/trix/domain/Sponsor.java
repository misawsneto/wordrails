package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Sponsor extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

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