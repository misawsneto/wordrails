package co.xarx.trix.services.post;

import co.xarx.trix.annotation.TimeIt;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.repository.SQLPostRepository;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ESPostSearchService implements PostSearchService {

	private ESPostService esPostService;
	private PostRepository postRepository;
	private SQLPostRepository sqlPostRepository;
	private StationPermissionService stationPermissionService;
	private PermissionFilterService permissionFilterService;

	@Autowired
	public ESPostSearchService(ESPostService esPostService, PostRepository postRepository,
							   SQLPostRepository sqlPostRepository, StationPermissionService stationPermissionService,
							   PermissionFilterService permissionFilterService) {
		this.esPostService = esPostService;
		this.postRepository = postRepository;
		this.sqlPostRepository = sqlPostRepository;
		this.stationPermissionService = stationPermissionService;
		this.permissionFilterService = permissionFilterService;
	}

	@Override
	public Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds) {
		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithReadPermission();

		if (CollectionUtils.isEmpty(stationsWithPermission) || CollectionUtils.isEmpty(postIds)) {
			return new ImmutablePair<Integer, List<PostView>>(0, new ArrayList<>());
		}

		BoolQueryBuilder mainQuery = esPostService.getBoolQueryBuilder(q, personId,
				Constants.Post.STATE_PUBLISHED, stationsWithPermission, postIds);

		Pageable pageable = new PageRequest(page, size);

		return esPostService.searchIndex(mainQuery, pageable, null);
	}


	@Override
	public Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate) {
		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithReadPermission();

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

	@TimeIt
	@Override
	public List<Post> search(PostStatement params, Integer page, Integer size) {
		Map<Integer, PostSearchResult> searchResults = searchIds(params);
		ArrayList<Post> emptyResult = new ArrayList<>();
		if (searchResults.isEmpty())
			return emptyResult;

		List<Integer> idsToGetFromDB = getPaginatedIds(new ArrayList<>(searchResults.keySet()), page, size);

		List<Post> result = postRepository.findAll(idsToGetFromDB);

		if(result == null)
			return emptyResult;

		return result;
	}

	private List<Integer> getPaginatedIds(List<Integer> ids, Integer page, Integer size) {
		int from = size * page;
		if(ids.size() < size)
			size = ids.size();
		return ids.subList(from, from + size);
	}

	@Override
	public List<PostData> searchData(PostStatement params, Integer page, Integer size) {
		Map<Integer, PostSearchResult> searchResults = searchIds(params);
		ArrayList<PostData> emptyResult = new ArrayList<>();
		if (searchResults.isEmpty())
			return emptyResult;

		List<Integer> idsToGetFromDB = getPaginatedIds(new ArrayList<>(searchResults.keySet()), page, size);
		List<PostData> datas = sqlPostRepository.findByIds(idsToGetFromDB);

		for (PostData data : datas) {
			PostSearchResult psr = searchResults.get(data.getId());
			data.setSnippet(psr.getSnippet());
		}
		
		return datas;
	}

	private Map<Integer, PostSearchResult> searchIds(PostStatement params) {
		List<PostSearchResult> results = esPostService.findIds(params);
		List<Integer> ids = results.stream()
				.map(PostSearchResult::getId)
				.collect(Collectors.toList());
		final List<Integer> filteredIds = permissionFilterService.filterIds(ids, Post.class, "read");
		return results.stream()
				.filter(r -> filteredIds.contains(r.getId()))
				.collect(Collectors.toMap(PostSearchResult::getId, Function.identity()));
	}
}
