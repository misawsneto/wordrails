package co.xarx.trix.services.post;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.security.PermissionFilterService;
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
public class PostSearchService {

	private ESPostService esPostService;
	private PostRepository postRepository;
	private PermissionFilterService filterService;
	private StationPermissionService stationPermissionService;

	@Autowired
	public PostSearchService(ESPostService esPostService, PostRepository postRepository,
							 PermissionFilterService filterService, StationPermissionService stationPermissionService) {
		this.esPostService = esPostService;
		this.postRepository = postRepository;
		this.filterService = filterService;
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

	public List<Post> search(List<Integer> ids, Pageable pageable) {
		List<Integer> idsToGetFromDB = ids.subList(pageable.getOffset(), pageable.getOffset() +
				pageable.getPageSize());

		List<Post> posts = postRepository.findAll(idsToGetFromDB);

		return posts;
	}

	@PostFilter("hasPermission(filterObject, 'co.xarx.trix.domain.Post', 'read')")
	public List<Integer> searchIds(PostSearchParams params, Pageable pageable) {
		List<FieldSortBuilder> sorts = new ArrayList<>();
		if(pageable.getSort() != null) {
			sorts.add(new FieldSortBuilder("date").order(SortOrder.DESC));
		}

		return esPostService.findIds(params, sorts);
	}
}
