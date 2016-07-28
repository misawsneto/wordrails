package co.xarx.trix.services.page;

import co.xarx.trix.api.v2.SectionData;
import co.xarx.trix.api.v2.request.SectionsUpdateRequest;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.persistence.SectionRepository;
import co.xarx.trix.web.rest.mapper.AbstractSectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {

	private SectionRepository sectionRepository;
	private AbstractSectionMapper sectionMapper;

	@Autowired
	public SectionService(SectionRepository sectionRepository, AbstractSectionMapper sectionMapper) {
		this.sectionRepository = sectionRepository;
		this.sectionMapper = sectionMapper;
	}


	@Transactional
	public List<Integer> saveSections(Page page, SectionsUpdateRequest sectionsUpdateRequest) {
		List<Integer> ids = new ArrayList<>();
		for (SectionData sectionData : sectionsUpdateRequest.getSections()) {
			AbstractSection section = sectionMapper.asAbstractEntity(sectionData);
			section.setPage(page);
			section = sectionRepository.save(section);
			ids.add(section.getId());
		}
		return ids;
	}


}
