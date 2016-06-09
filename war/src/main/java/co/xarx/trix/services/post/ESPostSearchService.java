package co.xarx.trix.services.post;

import co.xarx.trix.annotation.TimeIt;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.AbstractSearchService;
import co.xarx.trix.services.security.PermissionFilterService;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.ImmutablePage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ESPostSearchService extends AbstractSearchService implements PostSearchService {

	private ModelMapper mapper;
	private ESPostService esPostService;
	private PostRepository postRepository;
	private StationPermissionService stationPermissionService;
	private PermissionFilterService permissionFilterService;

	@Autowired
	public ESPostSearchService(ModelMapper mapper, ESPostService esPostService, PostRepository postRepository,
							   StationPermissionService stationPermissionService,
							   PermissionFilterService permissionFilterService) {
		this.mapper = mapper;
		this.esPostService = esPostService;
		this.postRepository = postRepository;
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
	public ImmutablePage<Post> search(PostStatement params, Integer page, Integer size) {
		Map<Integer, PostSearchResult> searchResults = searchIds(params);
		if (searchResults.isEmpty()) {
			return new ImmutablePage(null, page, 0);
		}

		List<Integer> idsToGetFromDB = getPaginatedIds(new ArrayList<>(searchResults.keySet()), page, size);

		List<Post> result = postRepository.findAll(idsToGetFromDB);

		if(result == null)
			return new ImmutablePage(null, page, 0);

		return new ImmutablePage(result, page, searchResults.size());
	}

	@TimeIt
	@Override
	public ImmutablePage<PostData> searchData(PostStatement params, Integer page, Integer size) {
		ImmutablePage<Post> posts = search(params, page, size);
		List<PostData> postDatas = getPostDatas(posts.items());
		return new ImmutablePage(postDatas, page, posts.totalSize());
	}

	private List<PostData> getPostDatas(List<Post> posts) {
		if (posts == null || posts.isEmpty())
			return new ArrayList<>();

		return posts.stream()
				.map(post -> mapper.map(post, PostData.class))
				.collect(Collectors.toList());
	}

//	@Override
//	public List<PostData> searchData(PostStatement params, Integer page, Integer size) {
//		Map<Integer, PostSearchResult> searchResults = searchIds(params);
//		ArrayList<PostData> emptyResult = new ArrayList<>();
//		if (searchResults.isEmpty())
//			return emptyResult;
//
//		List<Integer> idsToGetFromDB = getPaginatedIds(new ArrayList<>(searchResults.keySet()), page, size);
//		List<PostData> datas = sqlPostRepository.findByIds(idsToGetFromDB);
//
//		for (PostData data : datas) {
//			PostSearchResult psr = searchResults.get(data.getId());
//			data.setSnippet(psr.getSnippet());
//		}
//
//		return datas;
//	}

	private Map<Integer, PostSearchResult> searchIds(PostStatement params) {
		List<PostSearchResult> results = esPostService.findIds(params);
		return filterResultsByReadPermission(results);
	}

	private Map<Integer, PostSearchResult> filterResultsByReadPermission(List<PostSearchResult> results) {
		List<Integer> ids = results.stream()
				.map(PostSearchResult::getId)
				.collect(Collectors.toList());
		final List<Integer> filteredIds = permissionFilterService.filterIds(ids, Post.class, "read");

		return results.stream()
				.filter(r -> filteredIds.contains(r.getId()))
				.collect(Collectors.toMap(PostSearchResult::getId, Function.identity(),(v1, v2)->v1,
						LinkedHashMap::new));
	}
}
