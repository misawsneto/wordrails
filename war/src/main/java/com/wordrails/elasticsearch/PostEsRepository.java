package com.wordrails.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Created by jonas on 26/09/15.
 */
@Component
public class PostEsRepository{

	@Value("${elasticsearch.index}")
	private String indexName;

	private @Autowired @Qualifier("objectMapper")
	ObjectMapper objectMapper;

	private @Autowired @Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	private static final String ES_TYPE = "post";

	@Autowired
	private ElasticSearchService elasticSearchService;

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
		String doc = null;
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

//	public JSONObject makeObjectJson(Post post){
//		String doc = null;
//
//		try {
//			doc = objectMapper.writeValueAsString(makePostView(post, true));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		return (JSONObject) JSONValue.parse(doc);
//	}

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

//	public JSONObject convertToView(String json){
//		return (JSONObject) JSONValue.parse(json);
//	}

//	public JSONObject convertToView(String json, Map<String, HighlightField> highlightFieldMap){
//		JSONObject view = (JSONObject) JSONValue.parse(json);
//		for(String key: highlightFieldMap.keySet()){
//			String fragments = "";
//			for(Text fragment: highlightFieldMap.get(key).getFragments()){
//				fragments += fragment.string();
//			}
//			view.put(key, fragments);
//		}
//		return view;
//	}

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

	public PostIndexed findBySlug(String slug, List<Integer> readableIds) {

		BoolQueryBuilder mainQuery = boolQuery();
		mainQuery = mainQuery.must(matchQuery("slug", slug));

		BoolQueryBuilder statiosQuery = boolQuery();
		for(Integer stationId: readableIds){
			statiosQuery.should(
					matchQuery("stationId", String.valueOf(stationId)));
		}

		return null;
	}
}