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

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.api.v2.PersonalNotificationData;
import co.xarx.trix.api.v2.response.FileUpload;

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
	ContentResponse<List<TermView\>\> getAllTerms(@Query("taxonomyId")Integer taxonomyId, @Query("perspectiveId") Integer perspectiveId);
		
	@GET("/terms/allTerms")
	void getAllTerms(@Query("taxonomyId")Integer taxonomyId, @Query("perspectiveId") Integer perspectiveId, Callback<ContentResponse<List<TermView\>\>\> _callback);
		
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
    ("stationId") List<Integer\> sIds, @Query("state") String st, @Query("from") String from, @Query("until") String
    until, @Query("categoryId") List<Integer\> cIds, @Query("tag") List<String\> tags, @Query("page") Integer page,
    @Query("size") Integer size, @Query("order") List<String\> orders, @Query("embed") List<String\> embeds, @Query("snippet") String snippet);

    @GET("/v2/posts/search")
    void searchPosts(@Query("q") String q, @Query("authorId") List<Integer\> aIds, @Query("stationId") List<Integer\>
    sIds, @Query("state") String st, @Query("from") String from, @Query("until") String until, @Query("categoryId")
    List<Integer\> cIds, @Query("tag") List<String\> tags, @Query("page") Integer page, @Query("size") Integer size,
    @Query("order") List<String\> orders, @Query("embed") List<String\> embeds, @Query("snippet") String snippet, 
    Callback<ContentResponse<List<PostData\>\>\> _callback);

	@GET("/v2/bookmarks/search")
	ContentResponse<List<PostView\>\> searchBookmarks(@Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/v2/bookmarks/search")
	void searchBookmarks(@Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/v2/recommends/search")
	ContentResponse<List<PostView\>\> searchRecommends(@Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/v2/recommends/search")
	void searchRecommends(@Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
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
				@Query("page") int page, @Query("size") int size);
	
	@GET("/perspectives/termPerspectiveViews")
	void getTermPerspectiveView(@Query("termPerspectiveId") Integer termPerspectiveId, 
				@Query("termId") Integer termId, @Query("stationPerspectiveId") Integer stationPerspectiveId, 
				@Query("page") int page, @Query("size") int size, Callback<TermPerspectiveView> _callback);
				
				
	@GET("/perspectives/rowViews")
	RowView getRowView(@Query("stationPerspectiveId") Integer stationPerspectiveId, @Query("termPerspectiveId") Integer termPerspectiveId,
			@Query("childTermId") Integer childTermId, @Query("page") int page, @Query("size") int size);				
				
	@GET("/perspectives/rowViews")
	void getRowView(@Query("stationPerspectiveId") Integer stationPerspectiveId, @Query("termPerspectiveId") Integer termPerspectiveId,
			@Query("childTermId") Integer childTermId, @Query("page") int page, @Query("size") int size, Callback<RowView> _callback);				
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@PUT("/persons/me/password")
	@FormUrlEncoded
	Response putPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);
	
	@PUT("/persons/me/password")
	@FormUrlEncoded
	void putPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, Callback<Response> _callback);

	@GET("/posts/{postId}/getPostViewById")
	PostView getPostViewById(@Path("postId") Integer postId, @Query("withBody") Boolean withBody);

	@GET("/posts/{postId}/getPostViewById")
	void getPostViewById(@Path("postId") Integer postId, @Query("withBody") Boolean withBody, Callback<PostView> _callback);

	@GET("/posts/getPostViewBySlug")
	PostView getPostViewBySlug(@Query("slug") String slug, @Query("withBody") Boolean withBody);

	@GET("/posts/getPostViewBySlug")
	void getPostViewBySlug(@Query("slug") String slug,  @Query("withBody") Boolean withBody, Callback<PostView> _callback);

	@GET("/posts/{postId}/body")
	StringResponse getPostBody(@Path("postId") Integer postId);

	@GET("/posts/{postId}/body")
	void getPostBody(@Path("postId") Integer postId, Callback<StringResponse> _callback);

	@PUT("/v2/bookmarks/toggle/{postId}")
	BooleanResponse toggleBookmark(@Path("postId") Integer postId, @Body String body);
	
	@PUT("/v2/bookmarks/toggle/{postId}")
	void toggleBookmark(@Path("postId") Integer postId, @Body String body, Callback<BooleanResponse> _callback);
	
	@PUT("/v2/recommends/toggle/{postId}")
	BooleanResponse toggleRecommend(@Path("postId") Integer postId, @Body String body);
	
	@PUT("/v2/recommends/toggle/{postId}")
	void toggleRecommend(@Path("postId") Integer postId, @Body String body, Callback<BooleanResponse> _callback);
	
	@PUT("/persons/me/regId")
	@FormUrlEncoded
	Response putRegId(@Field("regId") String regId, @Field("networkId") Integer networkId, @Field("lat") Double lat, @Field("lng") Double lng);
	
	@PUT("/persons/me/regId")
	@FormUrlEncoded
	void putRegId(@Field("regId") String regId, @Field("networkId") Integer networkId,@Field("lat") Double lat, @Field("lng") Double lng, Callback<Response> _callback);
	
	@PUT("/mobile/location")
	@FormUrlEncoded
	Response putLocation(@Field("deviceCode") String deviceCode, @Field("device") String device, @Field("lat") Double lat, @Field("lng") Double lng);
	
	@PUT("/mobile/location")
	@FormUrlEncoded
	void putLocation(@Field("deviceCode") String deviceCode, @Field("device") String device, @Field("lat") Double lat, @Field("lng") Double lng, Callback<Response> _callback);
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	@GET("/networks/{id}/permissions")
	PersonPermissions getNetworkPersonPermissions(@Path("id") Integer id);
	
	@GET("/networks/{id}/permissions")
	void getNetworkPersonPermissions(@Path("id") Integer id, Callback<PersonPermissions> _callback);
	
	@GET("/persons/init")
	PersonData getInitialData();
	
	@GET("/persons/init")
	void getInitialData(Callback<PersonData> _callback);
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

/*---------------------------------------------------------------------------*/

$entities:IFacadeMethods(); separator="\n\n"$
}>>