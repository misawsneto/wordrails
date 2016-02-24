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

	public String orientation = Constants.Layout.SECTION_HORIZONTAL_ORIENTATION;

	@Min(value = 1)
	@Max(value = 100)
	public Integer pctSize;

	public Integer topMargin;
	public Integer leftMargin;
	public Integer bottomMargin;
	public Integer rightMargin;

	public Integer topPadding;
	public Integer leftPadding;
	public Integer bottomPadding;
	public Integer rightPadding;

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

	public Integer getPctSize() {
		return pctSize;
	}

	public void setPctSize(Integer pctSize) {
		this.pctSize = pctSize;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Integer getTopMargin() {
		return topMargin;
	}

	public void setMargin(Integer margin) {
		this.setTopMargin(margin);
		this.setLeftMargin(margin);
		this.setBottomMargin(margin);
		this.setRightMargin(margin);
	}

	public void setTopMargin(Integer topMargin) {
		this.topMargin = topMargin;
	}

	public Integer getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(Integer leftMargin) {
		this.leftMargin = leftMargin;
	}

	public Integer getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(Integer bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public Integer getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(Integer rightMargin) {
		this.rightMargin = rightMargin;
	}

	public Integer getTopPadding() {
		return topPadding;
	}

	public void setPadding(Integer padding) {
		this.setTopPadding(padding);
		this.setLeftPadding(padding);
		this.setBottomPadding(padding);
		this.setRightPadding(padding);
	}

	public void setTopPadding(Integer topPadding) {
		this.topPadding = topPadding;
	}

	public Integer getLeftPadding() {
		return leftPadding;
	}

	public void setLeftPadding(Integer leftPadding) {
		this.leftPadding = leftPadding;
	}

	public Integer getBottomPadding() {
		return bottomPadding;
	}

	public void setBottomPadding(Integer bottomPadding) {
		this.bottomPadding = bottomPadding;
	}

	public Integer getRightPadding() {
		return rightPadding;
	}

	public void setRightPadding(Integer rightPadding) {
		this.rightPadding = rightPadding;
	}

	@Override
	public String getType() {
		return Constants.Section.CONTAINER;
	}
}
