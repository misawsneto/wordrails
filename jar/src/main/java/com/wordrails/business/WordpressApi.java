package com.wordrails.business;

import java.util.Set;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


public interface WordpressApi {

    @GET("/posts")
    Set<WordpressPost> getPosts();

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
