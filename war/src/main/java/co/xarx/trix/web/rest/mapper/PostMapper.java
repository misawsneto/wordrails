package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.*;
import co.xarx.trix.domain.Post;

import java.util.HashSet;
import java.util.Set;

public class PostMapper implements Mapper<Post, PostData> {

	@Override
	public PostData convert(Post post) {
		PostData data = new PostData();

		data.setId(post.getId());

		data.setTitle(post.getTitle());
		data.setBody(post.getBody());
		data.setSubheading(post.getSubheading());
		data.setTopper(post.getTopper());
		data.setState(post.getState());

		data.setDate(post.getDate());
		data.setImageCaption(post.getImageCaptionText());
		data.setImageCredits(post.getImageCreditsText());
		data.setImageTitle(post.getImageTitleText());
		data.setImageLandscape(post.isImageLandscape());
		data.setLat(post.getLat());
		data.setLng(post.getLng());
		data.setNotified(post.isNotify());
		data.setSlug(post.getSlug());
		data.setScheduledDate(post.getScheduledDate());

		Set<CategoryData> categories = new HashSet<>();
		Set<String> tags = new HashSet<>();

		PersonData personData;
		ImageData image;
		VideoData video;

		return data;
	}

	private Set<CategoryData> categories;
	private Set<String> tags;

	private PersonData author;

	private ImageData image;
	private VideoData video;
}
