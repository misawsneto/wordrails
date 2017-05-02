package co.xarx.trix.api;

import co.xarx.trix.domain.Row;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RowView implements Serializable, Comparable<RowView> {
	private static final long serialVersionUID = 8344796806642658494L;

	public Integer id;
	public Integer termId;
	public String termName;
	public String type;
	public Integer index;
	public Integer termPerspectiveId;
	public List<CellView> cells;

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

	@Override
	public int compareTo(RowView o) {
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