package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.util.RestUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PostsApi;
import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor
public class V2PostsResource extends AbstractResource implements V2PostsApi {

	@Autowired
	private PostSearchService postSearchService;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;

	@Override
	public Response searchPosts(String query,
								List<Integer> authors,
								List<Integer> stations,
								String state,
								String from,
								String until,
								List<Integer> categories,
								List<String> tags,
								Integer page,
								Integer size,
								List<String> orders,
								List<String> embeds) {

		PostStatement params = new PostStatement(query, authors, stations, state, from, until, categories, tags, orders);

		List<Integer> ids = postSearchService.searchIds(params);
		List<Post> posts = postSearchService.search(ids, page, size);
		List<PostData> data = posts.stream()
				.map(post -> mapper.map(post, PostData.class))
				.collect(Collectors.toList());

		Set<String> postEmbeds = Sets.newHashSet("video", "image", "audio", "author", "categories");

		removeNotEmbeddedData(embeds, data, postEmbeds);

		Pageable pageable = RestUtil.getPageable(page, size, orders);
		Page p = new PageImpl(data, pageable, ids.size());

		return Response.ok().entity(p).build();
	}

	private void removeNotEmbeddedData(List<String> embeds, List<PostData> data, Set<String> postEmbeds) {
		for (String embed : postEmbeds) {
			if(!embeds.contains(embed)) {
				for (PostData postData : data) {
					try {
						Field field = PostData.class.getDeclaredField(embed);
						field.setAccessible(true);
						field.set(postData, null);
					} catch (NoSuchFieldException | IllegalAccessException e) {
						log.error("error to set data to null on embed", e);
					}
				}
			}
		}
	}

	@Override
	public ContentResponse<List<PostView>> findPostsByIds(List<Integer> ids) {
		List<Post> posts = postRepository.findAll(ids);
		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}
}
