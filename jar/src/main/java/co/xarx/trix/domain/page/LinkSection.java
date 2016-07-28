package co.xarx.trix.domain.page;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@lombok.Getter @lombok.Setter
@Entity
public class LinkSection extends AbstractSection implements Serializable {
	private static final long serialVersionUID = 5468718930497246401L;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "linkedsection_linkitem",
			joinColumns = @JoinColumn(name = "linksection_id"),
			inverseJoinColumns = @JoinColumn(name = "linkitem_id"))
	@MapKeyJoinColumn(name = "list_index", referencedColumnName = "list_index", nullable = false)
	private Map<Integer, LinkItem> linkItems;

	@Override
	public Type getType() {
		return Type.LINK;
	}

	@Override
	@JsonProperty("blocks")
	public List<Block> getBlocks() {
		if (linkItems == null)
			return new ArrayList<>();

		return linkItems
				.values()
				.stream()
				.map(s -> new BlockImpl<>(s, LinkItem.class))
				.collect(Collectors.toList());
	}
}
