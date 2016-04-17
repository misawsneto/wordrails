package co.xarx.trix.web.rest.map;

import co.xarx.trix.api.v2.PageData;
import co.xarx.trix.domain.page.Page;
import org.modelmapper.PropertyMap;

public class PageDataMap extends PropertyMap<Page, PageData> {

	@Override
	protected void configure() {
		map(source.getSectionList(), destination.getSections());
	}
}
