package co.xarx.trix.services.post;

import co.xarx.trix.api.PostView;
import co.xarx.trix.services.auth.StationPermissionService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.Logger;
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

@Service
public class PostSearchService {

	private ESPostService esPostService;
	private StationPermissionService stationPermissionService;

	@Autowired
	public PostSearchService(ESPostService esPostService, StationPermissionService stationPermissionService) {
		this.esPostService = esPostService;
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
}
