package com.wordrails.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.*;
import com.wordrails.util.WordrailsUtil;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by jonas on 26/09/15.
 */
@Component
public class PostEsRepository extends ElasticsearchBaseRepository {
	private @Autowired @Qualifier("objectMapper")
	ObjectMapper objectMapper;

	public SearchResponse runQuery(String query, FieldSortBuilder sort, Integer size){
		startEsClient();
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("posts")
				.setTypes("post")
				.setQuery(query);

		if (size != null && size > 0){
			searchRequestBuilder.setSize(size);
		}

		if (sort != null){
			searchRequestBuilder.addSort(sort);
		}

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		closeClient();
		return response;
	}

//	@Async
//	@Transactional
	public void save(Post post) {

		save(formatObjecJson(post), post.id.toString(), "posts", "post");
	}

	public void update(Post post){
		save(post);
	}

	public void delete(Post post){
		delete(post.id);
	}

	public void delete(Integer postId){
		delete(String.valueOf(postId), "posts", "post");
	}

	public String formatObjecJson(Post post){

		String doc = null;
		PostIndexed postIndexed = new PostIndexed();

		try {
			doc = objectMapper.writeValueAsString(makePostView(post, true));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		JSONObject toFormat = (JSONObject) JSONValue.parse(doc);

		return toFormat.toJSONString();
	}

	public PostIndexed makePostView(Post post, boolean addBody) {
		PostIndexed postView = makePostView(post);
		if(addBody)
			postView.body = post.body;
		return postView;
	}

	public JSONObject convertToView(String json){

//		JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
//		jsonObject.remove("author");
//		jsonObject.remove("station");
//		jsonObject.remove("terms");
//		jsonObject.remove("sponsorObj");
//		jsonObject.remove("comments");
//		jsonObject.remove("images");
//		return jsonObject;
		return (JSONObject) JSONValue.parse(json);
	}

	public PostIndexed makePostView(Post post){

		PostIndexed postView = new PostIndexed();
		postView.postId = post.id;
		postView.title = post.title;
		postView.subheading = post.subheading;
		postView.slug = post.slug;

		postView.sponsor = post.sponsor != null;
		postView.comments = post.comments;
		postView.images = post.images;
		postView.author = post.author;
		postView.station = post.station;
		postView.terms = post.terms;
//			postView.sponsorId = post.sponsor != null ? post.sponsor.id : null;
		postView.sponsorObj = post.sponsor;
		postView.smallId = post.imageSmallId;
		postView.mediumId = post.imageMediumId;
		postView.largeId = post.imageLargeId;

		postView.smallHash = post.imageSmallHash;
		postView.mediumHash = post.imageMediumHash;
		postView.largeHash = post.imageLargeHash;

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
		postView.snippet = WordrailsUtil.simpleSnippet(post.body, 100);
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
		postView.notify = post.notify;

		return postView;
	}
}