package co.xarx.trix.domain;


import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
public class Cell extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Min(0)
	@Column(name="\"index\"")
	public int index;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean featured;
	
	@NotNull
	@ManyToOne
	public Row row;
	
	@ManyToOne
	public Term term;
	
	@NotNull
	@ManyToOne
	public Post post;
}