IFacade(package, facade, entities) ::=

<<package $package$;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedString;
import com.google.gson.JsonObject;

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.api.v2.PersonalNotificationData;
import co.xarx.trix.api.v2.response.FileUpload;
import co.xarx.trix.api.v2.request.MessageFCM;

interface I$facade$ {
	@POST("/batch")
	Response postBatch(@Body List<Command> batch);

	@GET("/terms/search/findPostsByTagAndStationId")
	ContentResponse<List<PostView\>\> findPostsByTagAndStationId(@Query("tagName")String tagName, @Query("stationId")Integer stationId,
		@Query("page") int page, @Query("size") int size);

	@GET("/terms/search/findPostsByTagAndStationId")
	void findPostsByTagAndStationId(@Query("tagName")String tagName, @Query("stationId")Integer stationId,
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);

	@GET("/terms/search/findPostsByTerm")
        ContentResponse<List<PostView\>\> findPostsByTerm(@Query("termId")Integer termId,
            @Query("page") int page, @Query("size") int size);

    @GET("/terms/search/findPostsByTerm")
    void findPostsByTerm(@Query("termId")Integer termId,
        @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);

    @GET("/posts/search/findPostsByTags")
    ContentResponse<List<PostView\>\> findStationPostsByTags(@Query("tags")String tags, @Query("stationId")Integer stationId,
        @Query("page") int page, @Query("size") int size);

    @GET("/posts/search/findPostsByTags")
    void findStationPostsByTags(@Query("tags")String tags, @Query("stationId")Integer stationId,
        @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);

	@GET("/posts/{stationId}/findPostsAndPostsPromotedByBody")
	ContentResponse<List<PostView\>\> findPostsAndPostsPromotedByBody(@Path("stationId")Integer stationId, @Query("body") String body, 
		@Query("page") int page, @Query("size") int size);
		
	@GET("/posts/{stationId}/findPostsAndPostsPromotedByBody")
	void findPostsAndPostsPromotedByBody(@Path("stationId")Integer stationId, @Query("body") String body, 
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);

	@GET("/posts/{stationId}/findPostsAndPostsPromotedByTermId")
	ContentResponse<List<PostView\>\> findPostsAndPostsPromotedByTermId(@Path("stationId")Integer stationId, @Query("termId") Integer termId, 
		@Query("page") int page, @Query("size") int size);
		
	@GET("/posts/{stationId}/findPostsAndPostsPromotedByTermId")
	void findPostsAndPostsPromotedByTermId(@Path("stationId")Integer stationId, @Query("termId") Integer termId, 
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
		
	@GET("/terms/termTree")
	ContentResponse<List<TermDto\>\> getTermTree(@Query("perspectiveId") Integer perspectiveId);
		
	@GET("/terms/termTree")
	void getTermTree(@Query("perspectiveId") Integer perspectiveId, Callback<ContentResponse<List<TermDto\>\>\> _callback);

	@GET("/terms/allTerms")
	ContentResponse<List<TermView\>\> getAllTerms(@Query("taxonomyId")Integer taxonomyId, @Query("perspectiveId") Integer perspectiveId, @Query("stationId") Integer stationId);

	@GET("/terms/allTerms")
	void getAllTerms(@Query("taxonomyId")Integer taxonomyId, @Query("perspectiveId") Integer perspectiveId, @Query("stationId") Integer stationId, Callback<ContentResponse<List<TermView\>\>\> _callback);
		
	@GET("/posts/{stationId}/recent")
	ContentResponse<List<PostView\>\> findRecentPosts(@Path("stationId") Integer stationId, @Query("page") int page, @Query("size") int size);
	
	@GET("/posts/{stationId}/recent")
	void findRecentPosts(@Path("stationId") Integer stationId, @Query("page") int page, @Query("size") int size, 
	Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/posts/{stationId}/popular")
	ContentResponse<List<PostView\>\> findPopularPosts(@Path("stationId") Integer stationId, @Query("page") int page, @Query("size") int size);
	
	@GET("/posts/{stationId}/popular")
	void findPopularPosts(@Path("stationId") Integer stationId, @Query("page") int page, @Query("size") int size, 
	Callback<ContentResponse<List<PostView\>\>\> _callback);
		
	@GET("/posts/{stationId}/findPostsByStationIdAndAuthorIdAndState")
	ContentResponse<List<PostView\>\> findPostsByStationIdAndAuthorIdAndState(@Path("stationId") Integer stationId, @Query("authorId") Integer authorId, @Query("state") String state, 
			@Query("page") int page, @Query("size") int size);
		
	@GET("/posts/{stationId}/findPostsByStationIdAndAuthorIdAndState")
	void findPostsByStationIdAndAuthorIdAndState(@Path("stationId") Integer stationId, @Query("authorId") Integer authorId, @Query("state") String state, 
			@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);

	@GET("/posts/{stationId}/findPostsAndPostsPromotedByAuthorId")
	ContentResponse<List<PostView\>\> findPostsAndPostsPromotedByAuthorId(@Path("stationId")Integer stationId, @Query("authorId") Integer authorId, 
		@Query("page") int page, @Query("size") int size);
		
	@GET("/posts/{stationId}/findPostsAndPostsPromotedByAuthorId")
	void findPostsAndPostsPromotedByAuthorId(@Path("stationId")Integer stationId, @Query("authorId") Integer authorId, 
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/posts/{stationId}/searchPostsFromOrPromotedToStation")
	ContentResponse<SearchView\> searchPostsFromOrPromotedToStation(@Path("stationId")Integer stationId, @Query("query")String query,
		@Query("page") int page, @Query("size") int size);
	
	@GET("/posts/{stationId}/searchPostsFromOrPromotedToStation")
	void searchPostsFromOrPromotedToStation(@Path("stationId")Integer stationId, @Query("query")String query,
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<SearchView\>\> _callback);
		
		@PUT("/posts/{postId}/convert")
	ContentResponse<PostView\> convertPost(@Path("postId") Integer postId, @Query("state") String state);

	@POST("/auth/signin")
	@FormUrlEncoded
	Response socialLogin(@Field("provider") String provider, @Field("userId") String userId, @Field("accessToken") String accessToken);

	@POST("/auth/signin")
	@FormUrlEncoded
	void socialLogin(@Field("provider") String provider, @Field("userId") String userId, @Field("accessToken") String accessToken, Callback<Response> _callback);
	
	@PUT("/posts/{postId}/convert")
	@FormUrlEncoded
	void convertPost(@Path("postId") Integer postId, @Field("state") String state, Callback<ContentResponse<PostView\>\> _callback);

	@GET("/v2/posts/search")
    ContentResponse<List<PostData\>\> searchPosts(@Query("q") String q, @Query("authorId") List<Integer> aIds, @Query
    ("stationId") Integer sId, @Query
    ("stationIds") List<Integer\> sIds, @Query("state") String st, @Query("from") String from, @Query("until") String
    until, @Query("categoryId") List<Integer\> cIds, @Query("tags") List<String\> tags, @Query("excludePostIds") List<Integer\> excludePostIds, @Query("page") Integer page,
    @Query("size") Integer size, @Query("order") List<String\> orders, @Query("embed") List<String\> embeds, @Query("snippet") String snippet);

    @GET("/v2/posts/search")
    void searchPosts(@Query("q") String q, @Query("authorId") List<Integer\> aIds, @Query
    ("stationId") Integer sId, @Query("stationIds") List<Integer\>
    sIds, @Query("state") String st, @Query("from") String from, @Query("until") String until, @Query("categoryId")
    List<Integer\> cIds, @Query("tags") List<String\> tags, @Query("excludePostIds") List<Integer\> excludePostIds, @Query("page") Integer page, @Query("size") Integer size,
    @Query("order") List<String\> orders, @Query("embed") List<String\> embeds, @Query("snippet") String snippet, 
    Callback<ContentResponse<List<PostData\>\>\> _callback);

    @GET("/events/search")
    ResultEventSearch searchEvents(@Query("title") String title, @Query("period") String period, @Query("city") String city, @Query("categorieId") Integer categorieId, @Query("page") Integer page,
    @Query("size") Integer size, @Query("stationId") Integer stationId);

    @GET("/events/search")
    void searchEvents(@Query("title") String title, @Query("period") String period, @Query("city") String city, @Query("categorieId") Integer categorieId, @Query("page") Integer page,
    @Query("size") Integer size, @Query("stationId") Integer stationId, Callback<ResultEventSearch\> _callback);

    @GET("/events/{eventId}")
    EventView searchEventById(@Path("eventId") Integer postId, @Query("stationId") Integer stationId);

    @GET("/events/{eventId}")
    void searchEventById(@Path("eventId") Integer postId, @Query("stationId") Integer stationId, Callback<EventView\> _callback);

    @GET("/events/bookmarks")
    List<EventView\> searchEventBookmarks(@Query("page") Integer page, @Query("size") Integer size, @Query("userId") Integer userId);

    @GET("/events/bookmarks")
    void searchEventBookmarks(@Query("page") Integer page, @Query("size") Integer size, @Query("userId") Integer userId, Callback<List<EventView\>\> _callback);


	@GET("/v2/bookmarks/search")
	ContentResponse<List<PostView\>\> searchBookmarks(@Query("userId") Integer userId, @Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/v2/bookmarks/search")
	void searchBookmarks(@Query("userId") Integer userId, @Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/v2/recommends/search")
	ContentResponse<List<PostView\>\> searchRecommends(@Query("userId") Integer userId, @Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/v2/recommends/search")
	void searchRecommends(@Query("userId") Integer userId, @Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/notifications/searchNotifications")
	ContentResponse<List<PersonalNotificationData\>\> searchNotifications(@Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/notifications/searchNotifications")
	void searchNotifications(@Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PersonalNotificationData\>\>\> _callback);
		
	@GET("/posts/{stationId}/allPostRead")
	ContentResponse<List<PostView\>\> getAllPostReadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId,
		@Query("page") int page, @Query("size") int size);
	
	@GET("/posts/{stationId}/allPostRead")
	void getAllPostReadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId,
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
		
	@GET("/posts/{stationId}/postRead")
	ContentResponse<List<PostView\>\> getPostReadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId);
	
	@GET("/posts/{stationId}/postRead")
	void getPostReadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@GET("/files/{id}/contents")
	Response getFileContents(@Path("id") Integer id);
	
	@GET("/files/{id}/contents")
	Response getFileContents(@Path("id") Integer id, Callback<Void> _callback);

	@PUT("/files/{id}/contents")
	@Multipart
	Response putFileContents(@Path("id") Integer id, @Part("contents") TypedInput contents);
	
	@PUT("/files/{id}/contents")
	@Multipart
	void putFileContents(@Path("id") Integer id, @Part("contents") TypedInput contents, Callback<Void> _callback);	
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@GET("/perspectives/termPerspectiveDefinitions/{id}")
	TermPerspectiveView getTermPerspectiveDefinition(@Path("id") Integer id);

	@GET("/perspectives/termPerspectiveDefinitions/{id}")
	void getTermPerspectiveDefinition(@Path("id") Integer id, Callback<TermPerspectiveView> _callback);	
	
	
	@GET("/perspectives/termPerspectiveViews")
	TermPerspectiveView getTermPerspectiveView(@Query("termPerspectiveId") Integer termPerspectiveId, 
				@Query("termId") Integer termId, @Query("stationPerspectiveId") Integer stationPerspectiveId, 
				@Query("page") int page, @Query("size") int size, @Query("withBody") Boolean withBody, @Query("stationId") Integer stationId);
	
	@GET("/perspectives/termPerspectiveViews")
	void getTermPerspectiveView(@Query("termPerspectiveId") Integer termPerspectiveId, 
				@Query("termId") Integer termId, @Query("stationPerspectiveId") Integer stationPerspectiveId, 
				@Query("page") int page, @Query("size") int size, @Query("withBody") Boolean withBody, @Query("stationId") Integer stationId, Callback<TermPerspectiveView> _callback);
				
				
	@GET("/perspectives/rowViews")
	RowView getRowView(@Query("stationPerspectiveId") Integer stationPerspectiveId, @Query("termPerspectiveId") Integer termPerspectiveId,
			@Query("childTermId") Integer childTermId, @Query("stationId") Integer stationId, @Query("page") int page, @Query("size") int size);
				
	@GET("/perspectives/rowViews")
	void getRowView(@Query("stationPerspectiveId") Integer stationPerspectiveId, @Query("termPerspectiveId") Integer termPerspectiveId,
			@Query("childTermId") Integer childTermId, @Query("stationId") Integer stationId, @Query("page") int page, @Query("size") int size, Callback<RowView> _callback);
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@PUT("/persons/me/password")
	@FormUrlEncoded
	Response putPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);
	
	@PUT("/persons/me/password")
	@FormUrlEncoded
	void putPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, Callback<Response> _callback);

	@POST("/persons/password/recovery")
    Response postPasswordRecovery(@Body PasswordRecoveryDto body);

    @POST("/persons/password/recovery")
    void postPasswordRecovery(@Body PasswordRecoveryDto body, Callback<Response> _callback);
	
	@POST("/persons/password/update")
    Response postPasswordUpdate(@Body JsonObject body);
    
    @POST("/persons/password/update")
    void postPasswordUpdate(@Body JsonObject body, Callback<Response> _callback);

	@GET("/posts/{postId}/getPostViewById")
	PostView getPostViewById(@Path("postId") Integer postId, @Query("withBody") Boolean withBody, @Query("stationId") Integer stationId);

	@GET("/posts/{postId}/getPostViewById")
	void getPostViewById(@Path("postId") Integer postId, @Query("withBody") Boolean withBody, @Query("stationId") Integer stationId, Callback<PostView> _callback);

	@GET("/posts/getPostViewBySlug")
	PostView getPostViewBySlug(@Query("slug") String slug, @Query("withBody") Boolean withBody, @Query("stationId") Integer stationId);

	@GET("/posts/getPostViewBySlug")
	void getPostViewBySlug(@Query("slug") String slug,  @Query("withBody") Boolean withBody, @Query("stationId") Integer stationId, Callback<PostView> _callback);

		@GET("/posts/search/findBySlug")
    	ContentResponse<List<PostDto>\> findBySlug(@Query("slug") String slug, @Query("stationId") Integer stationId);

    	@GET("/posts/search/findBySlug")
    	void findBySlug(@Query("slug") String slug, @Query("stationId") Integer stationId, Callback<ContentResponse<List<PostDto>\>\> _callback);

    	@GET("/posts/search/findBySlug")
    	ContentResponse<List<PostProjectionDto>\> findBySlug_PostProjection(@Query("slug") String slug, @Query("stationId") Integer stationId, @Query("projection") String projection);

    	@GET("/posts/search/findBySlug")
    	void findBySlug_PostProjection(@Query("slug") String slug, @Query("projection") String projection, @Query("stationId") Integer stationId, Callback<ContentResponse<List<PostProjectionDto>\>\> _callback);

    	@GET("/posts/{id}")
        PostDto getPost(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId);

        @GET("/posts/{id}")
        void getPost(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId, Callback<PostDto> _callback);

        @GET("/posts/{id}/terms")
        ContentResponse<java.util.List<TermDto>\> getPostTerms(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId);

        @GET("/posts/{id}/terms")
        void getPostTerms(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId, Callback<ContentResponse<java.util.List<TermDto>\>\> _callback);

        @POST("/posts")
        Response postPost(@Body PostDto post, @Query("stationId") Integer stationId);

        @POST("/posts")
        void postPost(@Body PostDto post, @Query("stationId") Integer stationId, Callback<Void> _callback);

        @POST("/posts/{postId}/comments")
        CommentDto postComment(@Path("postId") Integer postId, @Body CommentDto comment, @Query("stationId") Integer stationId);

        @POST("/posts/{postId}/comments")
        void postComment(@Path("postId") Integer postId, @Body CommentDto comment, @Query("stationId") Integer stationId, Callback<CommentDto> _callback);

        @PUT("/posts/{id}/terms")
        @Headers("Content-Type: text/uri-list")
        Response putPostTerms(@Path("id") java.lang.Integer id, @Body TypedString terms, @Query("stationId") Integer stationId);

        @PUT("/posts/{id}/terms")
        @Headers("Content-Type: text/uri-list")
        void putPostTerms(@Path("id") java.lang.Integer id, @Body TypedString terms, @Query("stationId") Integer stationId, Callback<Void> _callback);

        @PUT("/posts/{id}/featuredImage")
        @Headers("Content-Type: text/uri-list")
        Response putPostFeaturedImage(@Path("id") java.lang.Integer id, @Body TypedString featuredImage, @Query("stationId") Integer stationId);

        @PUT("/posts/{id}/featuredImage")
        @Headers("Content-Type: text/uri-list")
        void putPostFeaturedImage(@Path("id") java.lang.Integer id, @Body TypedString featuredImage, @Query("stationId") Integer stationId, Callback<Void> _callback);

        @PATCH("/posts/{id}/terms")
        @Headers("Content-Type: text/uri-list")
        Response patchPostTerms(@Path("id") java.lang.Integer id, @Body TypedString terms, @Query("stationId") Integer stationId);

        @PATCH("/posts/{id}/terms")
        @Headers("Content-Type: text/uri-list")
        void patchPostTerms(@Path("id") java.lang.Integer id, @Body TypedString terms, @Query("stationId") Integer stationId, Callback<Void> _callback);

		@DELETE("/posts/{id}")
        Response deletePost(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId);

        @DELETE("/posts/{id}")
        void deletePost(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId, Callback<Void> _callback);

        @DELETE("/posts/{id}/featuredImage")
        Response deletePostFeaturedImage(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId);

        @DELETE("/posts/{id}/featuredImage")
        void deletePostFeaturedImage(@Path("id") java.lang.Integer id, @Query("stationId") Integer stationId, Callback<Void> _callback);



	@GET("/posts/{postId}/body")
	StringResponse getPostBody(@Path("postId") Integer postId, @Query("stationId") Integer stationId);

	@GET("/posts/{postId}/body")
	void getPostBody(@Path("postId") Integer postId, @Query("stationId") Integer stationId, Callback<StringResponse> _callback);

	@PUT("/v2/bookmarks/toggle/{postId}")
	BooleanResponse toggleBookmark(@Path("postId") Integer postId, @Query("stationId") Integer stationId, @Body String body);
	
	@PUT("/v2/bookmarks/toggle/{postId}")
	void toggleBookmark(@Path("postId") Integer postId, @Query("stationId") Integer stationId, @Body String body, Callback<BooleanResponse> _callback);
	
	@PUT("/v2/recommends/toggle/{postId}")
	BooleanResponse toggleRecommend(@Path("postId") Integer postId, @Query("stationId") Integer stationId, @Body String body);
	
	@PUT("/v2/recommends/toggle/{postId}")
	void toggleRecommend(@Path("postId") Integer postId, @Query("stationId") Integer stationId, @Body String body, Callback<BooleanResponse> _callback);
	
	@PUT("/persons/me/regId")
	@FormUrlEncoded
	Response putRegId(@Field("regId") String regId, @Field("networkId") Integer networkId, @Field("lat") Double lat, @Field("lng") Double lng);
	
	@PUT("/persons/me/regId")
	@FormUrlEncoded
	void putRegId(@Field("regId") String regId, @Field("networkId") Integer networkId,@Field("lat") Double lat, @Field("lng") Double lng, Callback<Response> _callback);
	
	@PUT("/mobile/location")
	@FormUrlEncoded
	Response putLocation(@Field("deviceCode") String deviceCode, @Field("deviceName") String deviceName, @Field("device") String device, @Field("lat") Double lat, @Field("lng") Double lng);
	
	@PUT("/mobile/location")
	@FormUrlEncoded
	void putLocation(@Field("deviceCode") String deviceCode, @Field("deviceName") String deviceName, @Field("device") String device, @Field("lat") Double lat, @Field("lng") Double lng, Callback<Response> _callback);
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@GET("/networks/{id}/permissions")
	PersonPermissions getNetworkPersonPermissions(@Path("id") Integer id);
	
	@GET("/networks/{id}/permissions")
	void getNetworkPersonPermissions(@Path("id") Integer id, Callback<PersonPermissions> _callback);
	
	@GET("/persons/init?version=2.2.0")
	PersonData getInitialData();
	
	@GET("/persons/init?version=2.2.0")
	void getInitialData(Callback<PersonData> _callback);

	@GET("/persons/search/findByUsername")
    PersonDto findByUsername(@Query("username") String username);

	@GET("/persons/search/findByUsername")
    void findByUsername(@Query("username") String username, Callback<PersonDto\> _callback);

    @GET("/persons/notifications")
    List<NotificationData\> getNotifications(@Query("page") Integer page, @Query("size") Integer size);

    @GET("/persons/notifications")
    void getNotifications(@Query("page") Integer page, @Query("size") Integer size, Callback<List<NotificationData\>\> _callback);
	
	@POST("/persons/create")
	PersonDto postCreatePerson(@Body PersonCreateDto personCreate);
	
	@POST("/persons/create")
	void postCreatePerson(@Body PersonCreateDto personCreate, Callback<PersonDto> _callback);
	
	@POST("/persons/update")
	@Multipart
	Response postPersonUpdate(@Part("user_id") int userId, @Part("username") String username, @Part("fullName") String fullName, @Part("bio") String bio, @Part("binaryImage") TypedInput binaryImage);
	
	@POST("/persons/update")
	@Multipart
	void postPersonUpdate(@Part("user_id") int userId, @Part("username") String username, @Part("fullName") String fullName, @Part("bio") String bio, @Part("binaryImage") TypedInput binaryImage, Callback<Response> _callback);
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@GET("/amazon/signedUrl")
	Response getAmazonSignedUrl(@Query("hash") String hash, @Query("type") String type);
	
	@GET("/amazon/signedUrl")
	void getAmazonSignedUrl(@Query("hash") String hash, @Query("type") String type, Callback<Response> _callback);
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@POST("/images/upload")
	@Multipart
	ImageUploadResponse postImageUpload(@Query("imageType") String imageType, @Part("contents") TypedInput contents);

	@POST("/images/upload")
	@Multipart
	void postImageUpload(@Query("imageType") String imageType, @Part("contents") TypedInput contents, Callback<ImageUploadResponse> _callback);

	@POST("/files/upload/video")
	@Multipart
	FileUpload postUploadVideo(@Part("contents") TypedInput contents);
	
	@POST("/files/upload/video")
	@Multipart
	void postUploadVideo(@Part("contents") TypedInput contents, Callback<FileUpload> _callback);

	@POST("/files/upload/audio")
	@Multipart
	FileUpload postUploadAudio(@Part("contents") TypedInput contents);
	
	@POST("/files/upload/audio")
	@Multipart
	void postUploadAudio(@Part("contents") TypedInput contents, Callback<FileUpload> _callback);

	@POST("/files/upload/doc")
	@Multipart
	FileUpload postUploadDoc(@Part("contents") TypedInput contents);
	
	@POST("/files/upload/doc")
	@Multipart
	void postUploadDoc(@Part("contents") TypedInput contents, Callback<FileUpload> _callback);

/*---------------------------------------------------------------------------*/

	@POST("/posts/{postId}/comments")
	CommentDto postComment(@Path("postId") Integer postId, @Body CommentDto comment);	

	@POST("/posts/{postId}/comments")
	void postComment(@Path("postId") Integer postId, @Body CommentDto comment, Callback<CommentDto> _callback);

	@DELETE("/comments/remove")
	Response deleteComment(@Query("commentId") Integer commentId, @Query("stationId") Integer stationId);

	@DELETE("/comments/remove")
	void deleteComment(@Query("commentId") Integer commentId, @Query("stationId") Integer stationId, Callback<Response> _callback);

	@GET("/comments/search/findPostCommentsOrderByDate")
    ContentResponse<List<CommentDto\>\> findPostCommentsOrderByDate(@Query("postId") Integer postId, @Query("stationId") Integer stationId, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort);

    @GET("/comments/search/findPostCommentsOrderByDate")
    void findPostCommentsOrderByDate(@Query("postId") Integer postId, @Query("stationId") Integer stationId, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort, Callback<ContentResponse<List<CommentDto\>\>\> _callback);

    @GET("/comments/search/findPostCommentsOrderByDate")
    ContentResponse<List<CommentProjectionDto\>\> findPostCommentsOrderByDate_CommentProjection(@Query("postId") Integer postId, @Query("stationId") Integer stationId, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort, @Query("projection") String projection);

    @GET("/comments/search/findPostCommentsOrderByDate")
    void findPostCommentsOrderByDate_CommentProjection(@Query("postId") Integer postId, @Query("stationId") Integer stationId, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort, @Query("projection") String projection, Callback<ContentResponse<List<CommentProjectionDto\>\>\> _callback);

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/

    @PUT("/v2/posts/{postId}/seen")
    Response putPostSeen(@Path("postId") Integer postId, @Query("timeReading") Integer timeReading, @Query("date") Long date, @Query("stationId") Integer stationId, @Body String str);

    @PUT("/v2/posts/{postId}/seen")
    void putPostSeen(@Path("postId") Integer postId, @Query("timeReading") Integer timeReading, @Query("date") Long date, @Query("stationId") Integer stationId, @Body String str, Callback<Response> _callback);

    @PUT("/v2/notifications/seen")
    Response putNotificationSeen(@Body MessageFCM messageFcm);

    @PUT("/v2/notifications/seen")
    void putNotificationSeen(@Body MessageFCM messageFcm, Callback<Response> _callback);

    @PUT("/v2/terms/{termId}/seen")
    Response putTermSeen(@Path("termId") Integer termId, @Query("timeReading") Integer timeReading, @Query("date") Long date, @Query("stationId") Integer stationId, @Body String str);

    @PUT("/v2/terms/{termId}/seen")
    void putTermSeen(@Path("termId") Integer termId, @Query("timeReading") Integer timeReading, @Query("date") Long date, @Query("stationId") Integer stationId, @Body String str, Callback<Response> _callback);

/*---------------------------------------------------------------------------*/

  @GET("/stations/{stationId}/geoAreas")
  GeoAreasView getStationGeoAreas(@Path("stationId") Integer stationId);

  @GET("/stations/{stationId}/geoAreas")
  void getStationGeoAreas(@Path("stationId") Integer stationId, Callback<GeoAreasView\> _callback);

  @POST("/stations/follow")
  List<StationView\> postFollowingStations(@Body List<Integer\> stationsIds);

  @POST("/stations/follow")
  void postFollowingStations(@Body List<Integer\> stationsIds, Callback<List<StationView\>\> _callback);

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/

  @GET("/classifieds/search")
  ContentResponse<List<ClassifiedView\>\> getClassifieds(@Query("stationId") Integer stationId, @Query("title") String title, @Query("order") String order, @Query("paymentMethods") List<String\> paymentMethods, @Query("categoryNames") List<String\> categoryNames, @Query("page") int page, @Query("size") int size, @Query("id") Integer id, @Query("lon") Double lon, @Query("lat") Double lat, @Query("radius") Double radius, @Query("unitOfLength") String unitOfLength);

  @GET("/classifieds/search")
  void getClassifieds(@Query("stationId") Integer stationId, @Query("title") String title, @Query("order") String order, @Query("paymentMethods") List<String\> paymentMethods, @Query("categoryNames") List<String\> categoryNames, @Query("page") int page, @Query("size") int size, @Query("id") Integer id, @Query("lon") Double lon, @Query("lat") Double lat, @Query("radius") Double radius, @Query("unitOfLength") String unitOfLength, Callback<ContentResponse<List<ClassifiedView\>\>\> _callback);

  @PUT("/classifieds/rating")
  RateClassifiedView putRateClassified(@Query("stationId") Integer stationId, @Body RateClassifiedView body);

  @PUT("/classifieds/rating")
  void putRateClassified(@Query("stationId") Integer stationId, @Body RateClassifiedView body, Callback<RateClassifiedView\> _callback);

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/

  @GET("/trixalerts/search")
  ContentResponse<List<TrixAlertView\>\> getTrixAlerts(@Query("page") int page, @Query("size") int size, @Query("deviceCode") String deviceCode);

  @GET("/trixalerts/search")
  void getTrixAlerts(@Query("page") int page, @Query("size") int size, @Query("deviceCode") String deviceCode, Callback<ContentResponse<List<TrixAlertView\>\>\> _callback);

  @PUT("/trixalerts/seen")
  void putSeenTrixAlert(@Body TrixSeenAlertview body, Callback<Response> _callback);

/*---------------------------------------------------------------------------*/

$entities:IFacadeMethods(); separator="\n\n"$
}>>