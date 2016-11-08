package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.MobileStats;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.query.statement.StatStatement;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.util.Constants;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class ESQueries {

	private Client client;
	private String accessIndex;

	private FileRepository fileRepository;
	private MobileDeviceRepository deviceRepository;
	private PersonPermissionService personPermissionService;

	@Autowired
	public ESQueries(Client client,
					 @Value("${elasticsearch.access_index}") String accessIndex, FileRepository fileRepository, MobileDeviceRepository deviceRepository, PersonPermissionService personPermissionService){

		this.client = client;
		this.accessIndex = accessIndex;
		this.fileRepository = fileRepository;
		this.deviceRepository = deviceRepository;
		this.personPermissionService = personPermissionService;
	}

	private Map<Long, Long> generalCounter(String queryName, QueryBuilder query, String orderField) {
		SearchRequestBuilder search = client.prepareSearch(accessIndex);

		search.setQuery(query);
		search.addAggregation(AggregationBuilders.dateHistogram(queryName).field(orderField).interval(DateHistogram.Interval.DAY));

		SearchResponse response = search.execute().actionGet();
		DateHistogram histogram = response.getAggregations().get(queryName);

		HashMap hist = new HashMap();

		for (DateHistogram.Bucket b : histogram.getBuckets()) {
			hist.put(b.getKeyAsDate().toDate().getTime(), b.getDocCount());
		}

		return hist;
	}

	public Map<Long, Long> getReadsByEntity(StatStatement statement){
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(statement.getESFieldName(), statement.getFieldId()));
		query.must(termQuery("action", Constants.StatsEventType.POST_READ));

		return generalCounter("reads_by_" + statement.getField(), query, "timestamp");
	}

	public Map<Long, Long> getRecommendsByEntity(StatStatement statement){
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(statement.getESFieldName(), statement.getFieldId()));
		query.must(termQuery("action", Constants.StatsEventType.POST_RECOMMEND));

		return generalCounter("comments_by_" + statement.getField(), query, "timestamp");
	}

	public Map<Long, Long> getCommentsByEntity(StatStatement statement){

		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(statement.getESFieldName(), statement.getFieldId()));
		query.must(termQuery("action", Constants.StatsEventType.POST_COMMENT));

		return generalCounter("comments_by_" + statement.getField(), query, "timestamp");
	}

	private SearchRequestBuilder prepareRangkingQuery(String field, String term, BoolQueryBuilder must, Integer size, Integer page){
		SearchRequestBuilder search = client.prepareSearch(accessIndex);
		search.addAggregation(AggregationBuilders
				.terms(term)
				.field(field)
				.size(size * (page + 1)));

		if(must != null) search.setQuery(must);

		return search;
	}

	private void joinMusts(BoolQueryBuilder must, StatStatement statement){
		must.must(termQuery("tenantId", TenantContextHolder.getCurrentTenantId()));
		must.must(rangeQuery("timestamp")
				.from(statement.getInterval().getStart().getMillis())
				.to(statement.getInterval().getEnd().getMillis())
				.includeLower(true)
				.includeUpper(true));

		for(int i = 0; i < statement.getByFields().size(); i++){
			must.must(termQuery(statement.getByFields().get(i), statement.getByValues().get(i)));
		}
	}

	public Map findMostPopular(StatStatement statement) throws Exception {
		String term = "popular_" + statement.getField();
		BoolQueryBuilder must = new BoolQueryBuilder();

		joinMusts(must, statement);

		SearchRequestBuilder search = prepareRangkingQuery(statement.getField(), term, must, statement.getSize(), statement.getPage());
		SearchResponse response = search.execute().actionGet();

		Terms terms = response.getAggregations().get(term);
		return parseRanking((List) terms.getBuckets(), statement.getSize(), statement.getPage());
	}

	public List<MobileStats> getMobileStats(Object tenantId, DateTime date){
		List<MobileStats> stats = new ArrayList<>();

		Interval week = new Interval(date.minusDays(7), date);
		Interval month = new Interval(date.minusDays(30), date);

		for(Constants.MobilePlatform app: Constants.MobilePlatform.values()){
			MobileStats appStats = new MobileStats();

			appStats.type = app;
			appStats.currentInstallations = (int) (long) deviceRepository.countDevicesByTenantIdAndType((String) tenantId, app);
			appStats.weeklyActiveUsers = getActiveUserByInterval(week, app);
			appStats.monthlyActiveUsers = getActiveUserByInterval(month, app);

			stats.add(appStats);
		}

		return stats;
	}


	public Integer getActiveUserByInterval(Interval interval, Constants.MobilePlatform type){
		return (int) (long) deviceRepository.countActiveDevices(
				TenantContextHolder.getCurrentTenantId(),
				type, interval.getStart().toString(),
				interval.getEnd().toString());
	}

	public Map<String, Integer> getFileStats(){
		List<Object[]> mimeSums = fileRepository.sumFilesSizeByMime(TenantContextHolder.getCurrentTenantId());
		Map<String, Integer> map = new HashMap<>();

		mimeSums.stream().filter(tuple -> tuple[0] != null && tuple[1] != null)
				.forEach(tuple -> map.put((String) tuple[0], (int) (long) tuple[1]));

		return map;
	}

	public List<Integer> getPersonIdsFromStation(Integer stationId){
		List<Person> persons = personPermissionService.getPersonFromStation(stationId, Permissions.READ);
		return persons.stream().map(Person::getId).collect(Collectors.toList());
	}

	private Map parseRanking(List<Terms.Bucket> results, Integer size, Integer page){
		Map buckets = new HashMap();

		int start = (size + 1) * page;
		int end = results.size();

		for (Terms.Bucket b : results.subList(start, end)) {
			buckets.put(b.getKey(), b.getDocCount());
		}
		return sortByValue(buckets);
	}

	private Map<String, Long> sortByValue(Map<String, Long> unsortMap) {

		LinkedList<Map.Entry<String, Long>> list =
				new LinkedList<Map.Entry<String, Long>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1,
							   Map.Entry<String, Long> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
		for (Map.Entry<String, Long> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public Integer countTotals(Object id, String field, String action) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		boolQuery.must(termQuery(field, id));
		boolQuery.must(termQuery("action", action));

		SearchRequestBuilder search = client.prepareSearch(accessIndex);
		search.setQuery(boolQuery);

		return (int) search.execute().actionGet().getHits().getTotalHits();
	}
}
