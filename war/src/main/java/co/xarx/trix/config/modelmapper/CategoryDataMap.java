package co.xarx.trix.config.modelmapper;

import co.xarx.trix.api.v2.CategoryData;
import co.xarx.trix.domain.Term;
import org.modelmapper.PropertyMap;

public class CategoryDataMap extends PropertyMap<Term, CategoryData> {

	@Override
	protected void configure() {
		map(source.getTaxonomyId(), destination.getTaxonomyId());
		map(source.getParent().getId(), destination.getParentId());
	}
}
