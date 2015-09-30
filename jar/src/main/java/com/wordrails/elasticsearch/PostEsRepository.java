package com.wordrails.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.*;
import com.wordrails.util.WordrailsUtil;
import org.elasticsearch.action.delete.DeleteResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by jonas on 26/09/15.
 */
@Component
public class PostEsRepository extends ElasticsearchBaseRepository {
	@Autowired @Qualifier("objectMapper")
	ObjectMapper objectMapper;

	@Async
	@Transactional
	public void save(Post post){

		save(formatObjecJson(post), post.id.toString(), "posts", "post");
	}

	public void delete(Post post){
		delete(post.id);
	}

	public void delete(Integer postId){
		delete(String.valueOf(postId), "posts", "post");
	}

	public String formatObjecJson(Post post){

		String doc = null;
		PostView postView = new PostView();

		try {
			doc = objectMapper.writeValueAsString(postView.makePostView(post));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		JSONObject toFormat = (JSONObject) JSONValue.parse(doc);

		return toFormat.toJSONString();
	}

	public List<Post> findPostsAndPostsPromotedByTermId(Integer stationId, Integer termId, Pageable pageable){
		return null;
	}

	private class PostView {

		public String title;
		public Integer postId;
		public Integer smallId;
		public Integer mediumId;
		public Integer largeId;
		public String smallHash;
		public String mediumHash;
		public String largeHash;
		public Boolean sponsor;
		@JsonManagedReference
		public Sponsor sponsorObj;
		@JsonManagedReference
		public Set<Comment> comments;
		@JsonManagedReference
		public Set<Image> images;
		@JsonManagedReference
		public Person author;
		@JsonManagedReference
		public Station station;
		@JsonManagedReference
		public Set<Term> terms;
		@JsonFormat(shape = JsonFormat.Shape.NUMBER)
		public java.util.Date date;
		public String snippet;
		public String body;
		public String topper;
		public String state;
		public int readsCount;
		public int favoritesCount;
		public int bookmarksCount;
		public int recommendsCount;
		public int commentsCount;

		public Integer imageId;
		public Integer imageSmallId;
		public Integer imageMediumId;
		public Integer imageLargeId;
		public Integer readTime;

		public boolean imageLandscape;

		public String authorName;
		public String authorUsername;
		public Integer authorId;

		public String authorEmail;
		public String authorTwitter;
		public Integer authorCoverMediumId;
		public Integer authorSmallImageId;
		public Integer authorImageSmallId;
		public String authorCoverMediumHash;
		public String authorImageSmallHash;

		public String stationName;
		public Integer stationId;

		public String slug;
		public String externalFeaturedImgUrl;
		public String externalVideoUrl;

		public String imageCaptionText;
		public String imageCreditsText;
		public String imageTitleText;


		public Double lat;

		public Double lng;

		public String subheading;

		@JsonFormat(shape = JsonFormat.Shape.NUMBER)
		public Date scheduledDate;

		public boolean notify;

		public PostView makePostView(Post post, boolean addBody) {
			PostView postView = makePostView(post);
			if(addBody)
				postView.body = post.body;
			return postView;
		}

		public PostView makePostView(Post post){

			PostView postView = new PostView();
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
}