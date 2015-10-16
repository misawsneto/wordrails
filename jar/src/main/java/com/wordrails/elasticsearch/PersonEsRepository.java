package com.wordrails.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.Person;
import com.wordrails.util.WordrailsUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by jonas on 26/09/15.
 */
@Component
public class PersonEsRepository {

	@Value("${elasticsearch.index}")
	private String indexName;

	private @Autowired @Qualifier("objectMapper")
	ObjectMapper objectMapper;

	private static final String ES_TYPE = "person";

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

		if (sort != null){
			searchRequestBuilder.addSort(sort);
		}

		return searchRequestBuilder.execute().actionGet();
	}

	public void save(Person person) {
		elasticSearchService.save(formatObjectJson(person), person.id.toString(), indexName, ES_TYPE);
	}

	public void update(Person person){
		elasticSearchService.update(formatObjectJson(person), person.id.toString(), indexName, ES_TYPE);
	}

	public void delete(Person person){
		delete(person.id);
	}

	public void delete(Integer postId){
		elasticSearchService.delete(String.valueOf(postId), indexName, ES_TYPE);
	}

	public String formatObjectJson(Person person){

		String doc = null;
		Person p = new Person();
		p.id = person.id;
		p.name = person.name;
		p.username = person.username;
		p.bio = person.bio;
		p.imageHash = person.imageHash;
		p.coverHash = person.coverHash;
		p.networkId = person.networkId;
		p.email = person.email;
		p.personsNetworkRoles = person.personsNetworkRoles;

		try {
			doc = objectMapper.writeValueAsString(person);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		return doc;
	}

	public JSONObject makeObjectJson(Person person){
		String doc = null;

		Person p = new Person();
		p.id = person.id;
		p.name = person.name;
		p.username = person.username;
		p.bio = person.bio;
		p.imageHash = person.imageHash;
		p.coverHash = person.coverHash;
		p.networkId = person.networkId;
		p.email = person.email;
		p.personsNetworkRoles = person.personsNetworkRoles;
		p.personsStationPermissions = person.personsStationPermissions;

		try {
			doc = objectMapper.writeValueAsString(p);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		return (JSONObject) JSONValue.parse(doc);
	}

//	public PostIndexed makePostView(Person person, boolean addBody) {
//		PostIndexed postView = makePostView(person);
//		return postView;
//	}

	public JSONObject convertToView(String json){
		return (JSONObject) JSONValue.parse(json);
	}

	public JSONObject convertToView(String json, Map<String, HighlightField> highlightFieldMap){
		JSONObject view = (JSONObject) JSONValue.parse(json);
		for(String key: highlightFieldMap.keySet()){
			String fragments = "";
			for(Text fragment: highlightFieldMap.get(key).getFragments()){
				fragments += fragment.string();
			}
			view.put(key, fragments);
		}
		return view;
	}

//	public PostIndexed makePostView(Person person){
//
//		PostIndexed postView = new PostIndexed();
//		postView.postId = post.id;
//		postView.title = post.title;
//		postView.subheading = post.subheading;
//		postView.slug = post.slug;
//
//		postView.sponsored = post.sponsor != null;
//		postView.comments = post.comments;
//		postView.images = post.images;
//		postView.author = post.author;
//		postView.station = post.station;
//		postView.terms = post.terms;
//		postView.sponsor = post.sponsor;
//		postView.smallId = post.imageSmallId;
//		postView.mediumId = post.imageMediumId;
//		postView.largeId = post.imageLargeId;
//
//		postView.smallHash = post.imageSmallHash;
//		postView.mediumHash = post.imageMediumHash;
//		postView.largeHash = post.imageLargeHash;
//
//		postView.imageId = post.imageId;
//		postView.imageSmallId = post.imageSmallId;
//		postView.imageMediumId = post.imageMediumId;
//		postView.imageLargeId = post.imageLargeId;
//
//		postView.imageLandscape = post.imageLandscape;
//		postView.date = post.date;
//		postView.topper = post.topper;
//		postView.readsCount = post.readsCount;
//		postView.recommendsCount = post.recommendsCount;
//		postView.commentsCount = post.commentsCount;
//		postView.snippet = WordrailsUtil.simpleSnippet(post.body, 100);
//		postView.authorName = post.author != null ? post.author.name : null;
//		postView.authorUsername = post.author != null ? post.author.username : null;
//		postView.authorCoverMediumId = post.author != null ? post.author.coverMediumId : null;
//		postView.authorImageSmallId = post.author != null ? post.author.imageSmallId : null;
//		postView.authorSmallImageId = post.author != null ? post.author.imageSmallId : null;
//		postView.authorImageSmallHash = post.author != null ? post.author.imageSmallHash : null;
//		postView.authorCoverMediumHash = post.author != null ? post.author.coverMediumHash : null;
//		postView.authorId = post.author != null ? post.author.id : null;
//		postView.authorEmail = post.author != null ? post.author.email : null;
//		postView.authorTwitter = post.author != null ? post.author.twitterHandle : null;
//		postView.externalFeaturedImgUrl = post.externalFeaturedImgUrl;
//		postView.externalVideoUrl = post.externalVideoUrl;
//		postView.readTime = post.readTime;
//		postView.state = post.state;
//		postView.scheduledDate = post.scheduledDate;
//		postView.lat = post.lat;
//		postView.lng = post.lng;
//		postView.imageCaptionText = post.imageCaptionText;
//		postView.imageCreditsText = post.imageCreditsText;
//		postView.imageTitleText = post.imageTitleText;
//		postView.stationName = post.station.name;
//		postView.stationId = post.station.id;
//		postView.notify = post.notify;
//
//		return postView;
//	}
}