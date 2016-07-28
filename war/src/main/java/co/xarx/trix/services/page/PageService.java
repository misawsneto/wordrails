package co.xarx.trix.services.page;

import co.xarx.trix.domain.page.Page;
import co.xarx.trix.persistence.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageService {

	private PageRepository pageRepository;

	@Autowired
	public PageService(PageRepository pageRepository) {
		this.pageRepository = pageRepository;
	}

	public Page findPage(Integer stationId, Integer pageId) {
		return pageRepository.findOne(stationId, pageId);
	}
}
