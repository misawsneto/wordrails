package com.wordrails.business;

import java.util.List;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


public interface WordpressApi {

    @GET("/posts")
    List<WordpressPost> getPosts();

    @POST("/posts")
    WordpressPost createPost(@Body WordpressPost task);

    @PUT("/posts/{id}")
    WordpressPost editPost(@Path("id") Integer id, @Body WordpressPost task);

    @DELETE("/posts/{id}")
    Response deletePost(@Path("id") Integer id);
}
