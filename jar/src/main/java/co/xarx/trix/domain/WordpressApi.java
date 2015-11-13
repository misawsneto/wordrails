package co.xarx.trix.domain;

import retrofit.client.Response;
import retrofit.http.*;

import java.util.Set;


public interface WordpressApi {

	@POST("/trix/posts")
	Set<WordpressPost> posts(@Body WordpressGetPostsParams params);

	@POST("/trix/syncerror")
	Set<WordpressPost> syncError(@Body String stacktrace);

	@GET("/taxonomies/post_tag/terms")
	Set<WordpressTerm> getTags();

	@GET("/taxonomies/category/terms")
	Set<WordpressTerm> getCategories();

	@POST("/posts")
	WordpressPost createPost(@Body WordpressPost task);

	@PUT("/posts/{id}")
	WordpressPost editPost(@Path("id") Integer id, @Body WordpressPost task);

	@DELETE("/posts/{id}")
	Response deletePost(@Path("id") Integer id);
}
