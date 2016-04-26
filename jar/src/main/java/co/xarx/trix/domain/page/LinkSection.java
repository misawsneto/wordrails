package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by misael on 4/25/2016.
 */
@lombok.Getter @lombok.Setter
@Entity
public class LinkSection {// extends AbstractSection implements Serializable {
	private static final long serialVersionUID = 5468718930497246401L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "linkedsection_linkitem",
			joinColumns = @JoinColumn(name = "linksection_id"),
			inverseJoinColumns = @JoinColumn(name = "linkitem_id"))
	@MapKey(name = "orderPosition")
	private Map<Integer, LinkItem> linkItems;
//
//	@Override
//	public String getType() {
//		return Constants.Section.LINK;
//	}
//
//	@Override
//	@JsonProperty("blocks")
//	public List<Block> getBlocks() {
//		ArrayList <Block> blocks = new ArrayList<Block>();
//		for (LinkItem item :linkItems.values()){
//			blocks.add(new BlockImpl<>(item, LinkItem.class));
//		}
//		return blocks;
//	}
}
