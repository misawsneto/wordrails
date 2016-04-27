package co.xarx.trix.domain.page;

import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
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

	@JsonProperty("blocks")
	public List<Block> getBlocks() {
		if (children == null)
			children = new HashMap<>();

		return new ArrayList(children.values());
	}

	public void setMargin(Integer margin) {
		this.setTopMargin(margin);
		this.setLeftMargin(margin);
		this.setBottomMargin(margin);
		this.setRightMargin(margin);
	}

	public void setPadding(Integer padding) {
		this.setTopPadding(padding);
		this.setLeftPadding(padding);
		this.setBottomPadding(padding);
		this.setRightPadding(padding);
	}

	@Override
	public String getType() {
		return Constants.Section.CONTAINER;
	}
}
