package co.xarx.trix.domain;


import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
public class Cell extends BaseEntity implements Comparable<Cell> {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Min(0)
	@Column(name="\"index\"")
	public Integer index;

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
		return true;
	}

	@Override
	public int compareTo(Cell o) {
		if (this.index != null && o.index != null)
			return Integer.compare(this.index, o.index);
		else if (this.index == null && o.index != null)
			return 1;
		else if (this.index != null && o.index == null)
			return -1;
		else if (this.index == null && o.index == null)
			return 1;

		else return 0;
	}
}