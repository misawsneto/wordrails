package co.xarx.trix.api.hal;

import co.xarx.trix.domain.page.Block;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class SectionHal extends HalResource {

	private String title;
	private String type;
	private String style;
	private Map<String, String> properties;
	private List<Block> blocks;
}
