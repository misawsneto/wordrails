package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.post.PostSearchParams;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PostApi;
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
public class V2PostsResource extends AbstractResource implements V2PostApi {

	@Autowired
	private PostSearchService postSearchService;
	@Autowired
	private ModelMapper mapper;

	@Override
	public Response searchPosts(String query,
								Integer author,
								List<Integer> stations,
								String state,
								String from,
								String until,
								List<Integer> categories,
								List<String> tags,
								Integer size,
								Integer page,
								List<String> orders,
								List<String> embeds) {

		Pageable pageable = getPageable(page, size, orders);

		PostSearchParams params = new PostSearchParams(query, author, stations, state, from, until, categories, tags);

		List<Integer> ids = postSearchService.searchIds(params, pageable);
		List<Post> posts = postSearchService.search(ids, pageable);
		List<PostData> data = posts.stream()
				.map(post -> mapper.map(post, PostData.class))
				.collect(Collectors.toList());

		Set<String> postEmbeds = Sets.newHashSet("video", "image", "audio", "author", "categories");

		removeNotEmbeddedData(embeds, data, postEmbeds);

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
}
