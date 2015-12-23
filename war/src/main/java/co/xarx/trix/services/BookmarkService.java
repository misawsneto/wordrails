package co.xarx.trix.services;

import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.elasticsearch.domain.ESBookmark;
import co.xarx.trix.elasticsearch.repository.ESBookmarkRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	@Autowired
	private ESBookmarkRepository esBookmarkRepository;
	@Autowired
	private ModelMapper modelMapper;

//	public List<PostView> searchIndex(BoolQueryBuilder boolQuery, Pageable pageable, SortBuilder sort) {
//
//	}

	public void saveIndex(Bookmark bookmark) {
		ESBookmark esBookmark = modelMapper.map(bookmark, ESBookmark.class);
		esBookmarkRepository.save(esBookmark);
	}

	public void deleteIndex(Integer id) {
		esBookmarkRepository.delete(id);
	}
}
