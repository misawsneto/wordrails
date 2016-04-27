package co.xarx.trix.services.page;

import co.xarx.trix.api.v2.request.SaveSectionsRequest;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.persistence.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {

	private SectionRepository sectionRepository;
	private SectionFactory sectionFactory;

	@Autowired
	public SectionService(SectionRepository sectionRepository, SectionFactory sectionFactory) {
		this.sectionRepository = sectionRepository;
		this.sectionFactory = sectionFactory;
	}


	@Transactional
	public List<Integer> saveSections(Page page, SaveSectionsRequest saveSectionsRequest) {
		List<Integer> ids = new ArrayList<>();
		for (SaveSectionsRequest.Section sectionData : saveSectionsRequest.getSections()) {
			AbstractSection section = sectionFactory.getSection(sectionData);
			section.setPage(page);
			section = sectionRepository.save(section);
			ids.add(section.getId());
		}
		return ids;
	}


}
