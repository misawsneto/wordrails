package co.xarx.trix.api.hal;

import lombok.Data;

@Data
public class QueryableSectionHal extends SectionHal {

	public boolean pageable;
	public Integer size;
}
