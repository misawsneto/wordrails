package com.wordrails.business;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


public interface WordpressApi {

    @POST("/posts")
    WordpressPost createPost(@Body WordpressPost task);

    @PUT("/posts/{id}")
    WordpressPost editPost(@Path("id") Integer id, @Body WordpressPost task);

    @DELETE("/posts/{id}")
    Response deletePost(@Path("id") Integer id);
}
