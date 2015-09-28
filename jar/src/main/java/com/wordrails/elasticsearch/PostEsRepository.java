package com.wordrails.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.Post;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

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

		_save(formatObjecJson(post), post.id.toString(), "posts", "post");
	}

	public String formatObjecJson(Post post){
		String doc = null;
		try {
			doc = objectMapper.writeValueAsString(post);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		JSONObject toFormat = (JSONObject) JSONValue.parse(doc);
		toFormat.remove("scheduledDate");
		toFormat.remove("externalFeaturedImgUrl");
		toFormat.remove("externalVideoUrl");
		toFormat.remove("imageId");
		toFormat.remove("imageSmallId");
		toFormat.remove("imageMediumId");
		toFormat.remove("imageLargeId");
		toFormat.remove("id");

		toFormat.put("authorId", post.author.id);

		return toFormat.toJSONString();
	}
}
