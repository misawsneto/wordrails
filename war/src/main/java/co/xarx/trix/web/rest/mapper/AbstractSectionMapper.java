package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.ContainerSectionData;
import co.xarx.trix.api.v2.QueryableSectionData;
import co.xarx.trix.api.v2.SectionData;
import co.xarx.trix.domain.page.AbstractSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbstractSectionMapper {

	@Autowired
	private ContainerSectionMapper containerSectionMapper;
	@Autowired
	private QueryableSectionMapper queryableSectionMapper;

	public AbstractSection asEntity(SectionData sectionData) {
		if (sectionData instanceof ContainerSectionData) {
			return containerSectionMapper.asEntity((ContainerSectionData) sectionData);
		} else if (sectionData instanceof QueryableSectionData) {
			return queryableSectionMapper.asEntity((QueryableSectionData) sectionData);
		}

		return null;
	}
}
