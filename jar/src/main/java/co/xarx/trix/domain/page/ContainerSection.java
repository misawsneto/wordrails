package co.xarx.trix.domain.page;

import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Map;

@Entity
@Table(name = "sectioncontainer")
@PrimaryKeyJoinColumn(name = "section_id", referencedColumnName = "id")
public class ContainerSection extends AbstractSection {

	private static final long serialVersionUID = 2651202755256597015L;

	@ManyToOne
	@JsonBackReference("parent")
	public ContainerSection parent;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "sectioncontainer_children", joinColumns = @JoinColumn(name = "container_id"))
	@MapKeyJoinColumn(name = "list_index", referencedColumnName = "list_index", nullable = false)
	public Map<Integer, AbstractSection> children;

	public boolean isParent() {
		return children != null && !children.isEmpty();
	}

	public boolean isChild() {
		return parent != null;
	}

	public ContainerSection getParent() {
		return parent;
	}

	public void setParent(ContainerSection parent) {
		this.parent = parent;
	}

	public Map<Integer, AbstractSection> getChildren() {
		return children;
	}

	public void setChildren(Map<Integer, AbstractSection> children) {
		this.children = children;
	}

	@Override
	public String getType() {
		return Constants.Section.CONTAINER;
	}
}
