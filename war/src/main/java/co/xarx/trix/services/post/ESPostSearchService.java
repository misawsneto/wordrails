package co.xarx.trix.services.post;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ESPostSearchService implements PostSearchService {

	private ESPostService esPostService;
	private PostRepository postRepository;
	private StationPermissionService stationPermissionService;

	@Autowired
	public ESPostSearchService(ESPostService esPostService, PostRepository postRepository,
							   StationPermissionService stationPermissionService) {
		this.esPostService = esPostService;
		this.postRepository = postRepository;
		this.stationPermissionService = stationPermissionService;
	}

	public Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds) {
		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithPermission();

		if (CollectionUtils.isEmpty(stationsWithPermission) || CollectionUtils.isEmpty(postIds)) {
			return new ImmutablePair<Integer, List<PostView>>(0, new ArrayList<>());
		}

		BoolQueryBuilder mainQuery = esPostService.getBoolQueryBuilder(q, personId,
				Constants.Post.STATE_PUBLISHED, stationsWithPermission, postIds);

		Pageable pageable = new PageRequest(page, size);

		return esPostService.searchIndex(mainQuery, pageable, null);
	}


	public Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate) {
		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithPermission();

		if (CollectionUtils.isEmpty(stationsWithPermission)) {
			return new ImmutablePair<Integer, List<PostView>>(0, new ArrayList<>());
		}

		BoolQueryBuilder mainQuery = esPostService.getBoolQueryBuilder(q, personId,
				Constants.Post.STATE_PUBLISHED, stationsWithPermission, null);

		FieldSortBuilder sort = null;
		if (sortByDate) {
			sort = new FieldSortBuilder("date").order(SortOrder.DESC);
		}

		Pageable pageable = new PageRequest(page, size);

		return esPostService.searchIndex(mainQuery, pageable, sort);
	}

	@Override
	public List<Post> search(List<Integer> ids, Integer page, Integer size) {
		int from = size * page;
		if(ids.isEmpty())
			return null;
		else if(ids.size() < size)
			size = ids.size();
		List<Integer> idsToGetFromDB = ids.subList(from, from + size);

		return postRepository.findAll(idsToGetFromDB);
	}

	@Override
	@PostFilter("hasPermission(filterObject, 'co.xarx.trix.domain.Post', 'read')")
	public List<Integer> searchIds(PostStatement params) {
		return esPostService.findIds(params);
	}
}
