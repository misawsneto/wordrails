package co.xarx.trix.services.elasticsearch;

import co.xarx.trix.domain.page.query.CommandBuilder;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class ESCommandBuilder implements CommandBuilder<ElasticSearchCommand> {

	private StationPermissionService stationPermissionService;

	@Autowired
	public ESCommandBuilder(StationPermissionService stationPermissionService) {
		this.stationPermissionService = stationPermissionService;
	}

	@Override
	public ElasticSearchCommand build(PostStatement statement) {
		ElasticSearchCommand esQuery = new ElasticSearchCommand();
		esQuery.setHighlightedField("body");

		List<FieldSortBuilder> fieldSortBuilders = new ArrayList<>();
		statement.getSorts().keySet().stream().forEach(sort -> {
			FieldSortBuilder fsb = SortBuilders.fieldSort(sort);
			if (statement.getSorts().get(sort))
				fsb.order(SortOrder.ASC);
			else
				fsb.order(SortOrder.DESC);
			fieldSortBuilders.add(SortBuilders.fieldSort(sort));
		});

		esQuery.setFieldSortBuilders(fieldSortBuilders);

		MultiMatchQueryBuilder queryText;
		BoolQueryBuilder mainQuery = boolQuery();
		mainQuery = mainQuery.must(matchQuery("state", Constants.Post.STATE_PUBLISHED));
		if (statement.isAllReadableStations()) {
			statement.setStationIds(new HashSet<>(stationPermissionService.findStationsWithPermission()));
		}

		BoolQueryBuilder stationQuery = boolQuery();
		for (Integer stationId : statement.getStationIds()) {
			stationQuery.should(matchQuery("stationId", String.valueOf(stationId)));
		}
		mainQuery = mainQuery.must(stationQuery);


		if(statement.getRichText() != null){
			queryText = multiMatchQuery(statement.getRichText())
					.field("body", 2)
					.field("title", 5)
					.field("topper")
					.field("subheading")
					.field("authorName")
					.field("categories.name")
					.prefixLength(1);

			mainQuery = mainQuery.must(queryText);
		}
		if(statement.getAuthorUsername() != null){
			mainQuery = mainQuery.must(
					matchQuery("authorUsername", statement.getAuthorUsername()));
		}
		if(CollectionUtils.isNotEmpty(statement.getTags())) {
			mainQuery = mainQuery.should(termsQuery("tags", statement.getTags()));
		}
		if(CollectionUtils.isNotEmpty(statement.getCategories())) {
			mainQuery = mainQuery.should(termsQuery("categories.id", statement.getCategories()));
		}

		esQuery.setBoolQueryBuilder(mainQuery);

		return esQuery;
	}
}
