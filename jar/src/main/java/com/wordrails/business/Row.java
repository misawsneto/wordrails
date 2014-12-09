package com.wordrails.business;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Row {
	public static final String FEATURED_ROW = "F";
	public static final String ORDINARY_ROW = "O";	
	public static final String SPLASHED_ROW = "S";	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@NotNull
	@Size(min=1, max=1)
	public String type;
	
	@OneToMany(mappedBy="row", cascade=CascadeType.ALL)
	public List<Cell> cells;
	
	
	
/*--FEATURED_ROW-------------------------------------------------------------*/	
	@OneToOne
	@JoinColumn(name="featuring_perspective") 
	public TermPerspective featuringPerspective;
/*--FEATURED_ROW-------------------------------------------------------------*/	

	
	
/*--SPLASHED_ROW-------------------------------------------------------------*/	
	@OneToOne
	@JoinColumn(name="splashed_perspective") 
	public TermPerspective splashedPerspective;
/*--SPLASHED_ROW-------------------------------------------------------------*/	

	
	
/*--ORDINARY_ROW-------------------------------------------------------------*/	
	@Min(0)
	@Column(name="\"index\"")
	public Integer index;
	
	@ManyToOne
	public Term term;
	
	@ManyToOne
	public TermPerspective perspective;
/*--ORDINARY_ROW-------------------------------------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Row other = (Row) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}