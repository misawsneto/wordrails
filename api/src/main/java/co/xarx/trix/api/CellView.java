package co.xarx.trix.api;

import co.xarx.trix.domain.Row;

import java.io.Serializable;

public class CellView implements Serializable, Comparable<CellView>{
	private static final long serialVersionUID = -9007636043152596902L;

	public Integer id;
	public int index;
	public PostView postView;

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
		if (index > 0) {
			if (other.index != null)
				return false;
		} else if (index == other.index)
			return false;
		return true;
	}

	@Override
	public int compareTo(CellView o) {
		return Integer.compare(this.index, o.index);
	}

}