package co.xarx.trix.domain;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Cell extends BaseEntity {

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