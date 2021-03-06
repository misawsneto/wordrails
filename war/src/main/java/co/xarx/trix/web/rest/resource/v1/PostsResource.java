package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.*;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.QPost;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.PostApi;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor
public class PostsResource extends AbstractResource implements PostApi {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private PostSearchService postSearchService;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	PostService postService;

	@Deprecated
	public ContentResponse<SearchView> searchPosts(
			String q,
			String stationIds,
			Integer personId,
			String publicationType,
			boolean noHighlight,
			boolean sortByDate,
			Integer page,
			Integer size) {

		if (q == null) q = "";

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, personId, page, size, sortByDate);

		return getSearchView(postsViews);
	}

	public void getPosts() throws ServletException, IOException {
		forward();
	}

	@Override
	public void findBySlug() throws ServletException, IOException {
		forward();
	}

	public Response copyToStation(CopyPostsDto copyPostsDto) throws ServletException, IOException{
		boolean success = postService.copyToStation(copyPostsDto);
		return  success ? Response.status(Response.Status.OK).build() : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	@Override
	public void getComments(Integer postId) throws ServletException, IOException {
		forward();
	}

	public void getPost(int postId) throws ServletException, IOException {
		forward();
	}
	public void putPost(Integer id) throws ServletException, IOException {
		if(id != null)
			cacheManager.getCache("postViewById").evict(id);
		forward();
	}

	public void postPost() throws ServletException, IOException {
		forward();
	}

	public void deletePost(Integer id) throws ServletException, IOException {
		forward();
	}

	public void postComment(@P("p") Integer postId) throws ServletException,
			IOException {
		forward("/comments");
	}

	public void getTerms(@P("p") Integer postId) throws ServletException,
			IOException {
		forward();
	}

	public void putComment(@P("p") Integer postId, Integer commentId) throws ServletException,
			IOException {
		forward("/comments/" + commentId);
	}

	@Transactional
	public ContentResponse<PostView> updatePostTerms(Integer postId, List<TermDto> terms) throws ServletException, IOException {

		Post post = postRepository.findOne(postId);

		ContentResponse<PostView> response = new ContentResponse<>();
		response.content = postConverter.convertTo(post);
		return response;
	}

	@Transactional
	public PostView getPostViewBySlug(String slug, Boolean withBody) throws ServletException, IOException {
		return postService.getPostViewBySlug(slug, withBody);
	}

	@Transactional
	public PostView getPostViewById(Integer postId, Boolean withBody) throws ServletException, IOException {
		PostView postView = cacheManager.getCache("postViewById").get(postId, PostView.class);
		if(postView == null){
			Post post = postRepository.findOne(postId);
			if (post != null) {
				postView = postConverter.convertTo(post);
				postView.body = post.body;
				cacheManager.getCache("postViewById").put(postId, postView);
			}
		}

		if(postView != null && (withBody == null || !withBody)) {
			postView.body = null;
		}

		return postView;
	}

	public ContentResponse<List<PostView>> findPostsByStationIdAndAuthorIdAndState(Integer stationId,
																				   Integer authorId,
																				   String state,
																				   int page,
																				   int size) throws ServletException, IOException {

		Pageable pageable = new PageRequest(page, size);

		QPost p = QPost.post;

		Page<Post> pagePosts = postRepository.findAll(p.station.id.eq(stationId).and(p.author.id.eq(authorId)), pageable);
		List<Post> posts = Lists.newArrayList(pagePosts.iterator());

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	private ContentResponse<SearchView> getSearchView(Pair<Integer, List<PostView>> postsViews) {
		ContentResponse<SearchView> response = new ContentResponse<>();
		response.content = new SearchView();
		response.content.hits = postsViews.getLeft();
		response.content.posts = postsViews.getRight();
		return response;
	}

	public ContentResponse<List<PostView>> getRecent(Integer stationId,
													 Integer page,
													 Integer size) {
		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPostsOrderByDateDesc(stationId, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	public StringResponse getPostBody(Integer postId){
		String body = postRepository.findPostBodyById(postId);

		StringResponse content = new StringResponse();
		content.response = body;
		return content;
	}

	public ContentResponse<List<PostView>> findPostsByTagAndStationId(String tagsString,
																	  Integer stationId,
																	  int page,
																	  int size) throws ServletException, IOException {
		if (tagsString == null || tagsString.isEmpty()) {
			throw new BadRequestException("Tags list is empty or null");
		}

		Set<String> tags = new HashSet<>(Arrays.asList(tagsString.split(",")));

		List<Post> posts;

		if(stationId == null){
			posts = queryPersistence.findPostsByTag(tags, page, size);
		} else {
			posts = queryPersistence.findPostsByTagAndStationId(tags, stationId, page, size);
		}

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@Override
	public ContentResponse<List<PostView>> findPostsByIds(List<Integer> ids) {
		List<Post> posts = postRepository.findAll(ids);
		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@Override
	public void putPostTerms(Integer id) throws IOException {
		forward();
	}
}
