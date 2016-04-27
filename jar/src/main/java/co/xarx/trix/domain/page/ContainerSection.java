package co.xarx.trix.domain.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sectioncontainer")
@PrimaryKeyJoinColumn(name = "section_id", referencedColumnName = "id")
public class ContainerSection extends AbstractSection {

	private static final long serialVersionUID = 2651202755256597015L;

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "sectioncontainer_children", joinColumns = @JoinColumn(name = "container_id"))
	@MapKeyJoinColumn(name = "list_index", referencedColumnName = "list_index", nullable = false)
	public Map<Integer, AbstractSection> children;

	@JsonProperty("blocks")
	public List<Block> getBlocks() {
		if (children == null)
			return new ArrayList<>();

		return children
				.values()
				.stream()
				.map(s -> new BlockImpl<Section>(s, Section.class))
				.collect(Collectors.toList());
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

	public void addChild(AbstractSection section) {
		if (children == null)
			children = new HashMap<>();

		children.put(children.size(), section);
	}

	@Override
	public Type getType() {
		return Type.CONTAINER;
	}
}
