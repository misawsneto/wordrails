package co.xarx.trix.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cell extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

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