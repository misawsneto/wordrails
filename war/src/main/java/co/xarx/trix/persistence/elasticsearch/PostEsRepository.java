package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.api.PostView;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.query.ElasticSearchExecutor;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.util.TrixUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.utils.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("post_executor")
public class PostEsRepository implements ElasticSearchExecutor<PostView> {

	@Value("${elasticsearch.index}")
	private String indexName;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PostConverter postConverter;

	private static final String ES_TYPE = "post";

	@Autowired
	private ElasticSearchService elasticSearchService;

	public List<PostView> execute(ElasticSearchQuery query, Integer size, Integer from) {
		SearchRequestBuilder searchRequestBuilder = elasticSearchService
				.getElasticsearchClient()
				.prepareSearch(indexName)
				.setTypes(ES_TYPE)
				.setQuery(query.getBoolQueryBuilder())
				.setSize(size)
				.setFrom(from);

		searchRequestBuilder.addHighlightedField(query.getHighlightedField(), 100, 4);
		searchRequestBuilder.setHighlighterPreTags("{snippet}");
		searchRequestBuilder.setHighlighterPostTags("{#snippet}");

		if (Lists.isNotEmpty(query.getFieldSortBuilders())){
			searchRequestBuilder.addSort(new FieldSortBuilder("_score").order(SortOrder.DESC));
			List<FieldSortBuilder> sortBuilders = query.getFieldSortBuilders();
			sortBuilders.stream().forEach(searchRequestBuilder::addSort);
		}

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		return getViews(searchResponse.getHits().hits());
	}

	public SearchResponse runQuery(String query, FieldSortBuilder sort, Integer size, Integer page, String highlightedField){
		SearchRequestBuilder searchRequestBuilder = elasticSearchService
														.getElasticsearchClient()
														.prepareSearch(indexName)
														.setTypes(ES_TYPE)
														.setQuery(query);

		if (size != null && size > 0){
			searchRequestBuilder.setSize(size);

			if (page != null){
				searchRequestBuilder.setFrom(page * size);
			}
		}

		if (highlightedField != null){
			searchRequestBuilder.addHighlightedField(highlightedField, 100, 4);
			searchRequestBuilder.setHighlighterPreTags("{snippet}");
			searchRequestBuilder.setHighlighterPostTags("{#snippet}");
		}

		if (sort != null){
			searchRequestBuilder.addSort(new FieldSortBuilder("_score").order(SortOrder.DESC));
			searchRequestBuilder.addSort(sort);
		}

		return searchRequestBuilder.execute().actionGet();
	}

	public List<PostView> getViews(SearchHit... hits) {
		List<PostView> views = new ArrayList<>();

		for (SearchHit hit : hits) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				final PostView postView = objectMapper.readValue(
						objectMapper.writeValueAsString(
								objectMapper.readValue(
										hit.getSourceAsString(), PostView.class)), PostView.class);

				Map<String, HighlightField> highlights = hit.getHighlightFields();
				if (highlights != null && highlights.get("body") != null) {
					StringBuilder sb = new StringBuilder();
					for (Text fragment : highlights.get("body").getFragments()) {
						sb.append(fragment.toString()).append(" ");
					}

					postView.snippet = sb.toString();
				} else {
					postView.snippet = TrixUtil.simpleSnippet(postView.body, 100);
				}

				postView.snippet = TrixUtil.htmlStriped(postView.snippet);
				postView.snippet = postView.snippet.replaceAll("\\{snippet\\}", "<b>").replaceAll("\\{#snippet\\}", "</b>");
				views.add(postView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return views;
	}

	public void save(Post post) {
		elasticSearchService.save(formatObjectJson(post), post.id.toString(), indexName, ES_TYPE);
	}

	public void update(Post post){
		elasticSearchService.update(formatObjectJson(post), post.id.toString(), indexName, ES_TYPE);
	}

	public void delete(Post post){
		delete(post.id);
	}

	public void delete(Integer postId){
		elasticSearchService.delete(String.valueOf(postId), indexName, ES_TYPE);
	}

	public String formatObjectJson(Post post){
		String doc;
		try {
			doc = objectMapper.writeValueAsString(makePostView(post, true));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		//JSONObject toFormat = (JSONObject) JSONValue.parse(doc);
		//return toFormat.toJSONString();

		return doc;
	}

	public PostIndexed makePostView(Post post, boolean addBody) {
		PostIndexed postView = makePostView(post);
		if(addBody)
			postView.body = post.body;
		return postView;
	}

	public PostIndexed convertToView(String json){
		try {
			return objectMapper.readValue(json, PostIndexed.class);
		} catch (IOException e) {
			return null;
		}
	}

	public PostIndexed makePostView(Post post){

		PostIndexed postView = new PostIndexed();
		postView.postId = post.id;
		postView.title = post.title;
		postView.subheading = post.subheading;
		postView.slug = post.slug;

		postView.sponsored = post.sponsor != null;
		postView.comments = post.comments;
		postView.images = post.images;
		postView.author = post.author;
		postView.station = post.station;
		postView.terms = post.terms;
		postView.sponsor = post.sponsor;
		postView.smallId = post.imageSmallId;
		postView.mediumId = post.imageMediumId;
		postView.largeId = post.imageLargeId;

		postView.smallHash = postView.imageSmallHash = post.imageSmallHash;
		postView.mediumHash = postView.imageMediumHash = post.imageMediumHash;
		postView.largeHash = postView.imageLargeHash = post.imageLargeHash;

		postView.imageId = post.imageId;
		postView.imageSmallId = post.imageSmallId;
		postView.imageMediumId = post.imageMediumId;
		postView.imageLargeId = post.imageLargeId;

		postView.imageLandscape = post.imageLandscape;
		postView.date = post.date;
		postView.topper = post.topper;
		postView.readsCount = post.readsCount;
		postView.recommendsCount = post.recommendsCount;
		postView.commentsCount = post.commentsCount;
		postView.authorName = post.author != null ? post.author.name : null;
		postView.authorUsername = post.author != null ? post.author.username : null;
		postView.authorCoverMediumId = post.author != null ? post.author.coverMediumId : null;
		postView.authorImageSmallId = post.author != null ? post.author.imageSmallId : null;
		postView.authorSmallImageId = post.author != null ? post.author.imageSmallId : null;
		postView.authorImageSmallHash = post.author != null ? post.author.imageSmallHash : null;
		postView.authorCoverMediumHash = post.author != null ? post.author.coverMediumHash : null;
		postView.authorId = post.author != null ? post.author.id : null;
		postView.authorEmail = post.author != null ? post.author.email : null;
		postView.authorTwitter = post.author != null ? post.author.twitterHandle : null;
		postView.externalFeaturedImgUrl = post.externalFeaturedImgUrl;
		postView.externalVideoUrl = post.externalVideoUrl;
		postView.readTime = post.readTime;
		postView.state = post.state;
		postView.scheduledDate = post.scheduledDate;
		postView.lat = post.lat;
		postView.lng = post.lng;
		postView.imageCaptionText = post.imageCaptionText;
		postView.imageCreditsText = post.imageCreditsText;
		postView.imageTitleText = post.imageTitleText;
		postView.stationName = post.station.name;
		postView.stationId = post.station.id;
		postView.stationIdString = post.station.id + "";
		postView.notify = post.notify;

		return postView;
	}
}