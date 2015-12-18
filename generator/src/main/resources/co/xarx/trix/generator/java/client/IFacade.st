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

interface I$facade$ {
	@POST("/batch")
	Response postBatch(@Body List<Command> batch);

	@GET("/terms/search/findPostsByTagAndStationId")
	ContentResponse<List<PostView\>\> findPostsByTagAndStationId(@Query("tagName")String tagName, @Query("stationId")Integer stationId,
		@Query("page") int page, @Query("size") int size);

	@GET("/terms/search/findPostsByTagAndStationId")
	void findPostsByTagAndStationId(@Query("tagName")String tagName, @Query("stationId")Integer stationId,
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
		
	@GET("/posts/search/networkPosts")
	ContentResponse<SearchView\> searchPosts(@Query("query") String q, @Query("stationIds") String sIds, @Query("personId") Integer pId,
			@Query("publicationType") String pt, @Query("noHighlight") Boolean nh, @Query("sortByDate") Boolean sd,
			@Query("page") Integer p, @Query("size") Integer s);
	
	@GET("/posts/search/networkPosts")
	void searchPosts(@Query("query") String q, @Query("stationIds") String sIds, @Query("personId") Integer pId,
			@Query("publicationType") String pt, @Query("noHighlight") Boolean nh, @Query("sortByDate") Boolean sd,
			@Query("page") Integer p, @Query("size") Integer s, Callback<ContentResponse<SearchView\>\> _callback);
		
	@GET("/bookmarks/searchBookmarks")
	ContentResponse<List<PostView\>\> searchBookmarks(@Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/bookmarks/searchBookmarks")
	void searchBookmarks(@Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/recommends/searchRecommends")
	ContentResponse<List<PostView\>\> searchRecommends(@Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/recommends/searchRecommends")
	void searchRecommends(@Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
	@GET("/notifications/searchNotifications")
	ContentResponse<List<NotificationView\>\> searchNotifications(@Query("query") String query, @Query("page") int page, @Query("size") int size);
	
	@GET("/notifications/searchNotifications")
	void searchNotifications(@Query("query") String query, @Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<NotificationView\>\>\> _callback);
		
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

	@PUT("/bookmarks/toggleBookmark")
	@FormUrlEncoded
	BooleanResponse toggleBookmark(@Field("postId") Integer postId);
	
	@PUT("/bookmarks/toggleBookmark")
	@FormUrlEncoded
	void toggleBookmark(@Field("postId") Integer postId, Callback<BooleanResponse> _callback);
	
	@PUT("/recommends/toggleRecommend")
	@FormUrlEncoded
	BooleanResponse toggleRecommend(@Field("postId") Integer postId);
	
	@PUT("/recommends/toggleRecommend")
	@FormUrlEncoded
	void toggleRecommend(@Field("postId") Integer postId, Callback<BooleanResponse> _callback);
	
	@PUT("/persons/me/regId")
	@FormUrlEncoded
	Response putRegId(@Field("regId") String regId, @Field("networkId") Integer networkId, @Field("lat") Double lat, @Field("lng") Double lng);
	
	@PUT("/persons/me/regId")
	@FormUrlEncoded
	void putRegId(@Field("regId") String regId, @Field("networkId") Integer networkId, @Field("lat") Double lat, @Field("lng") Double lng, Callback<Response> _callback);
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
	Response postImageUpload(@Query("imageType") String imageType, @Part("contents") TypedInput contents);

	@POST("/images/upload")
	@Multipart
	void postImageUpload(@Query("imageType") String imageType, @Part("contents") TypedInput contents, Callback<Response> _callback);
/*---------------------------------------------------------------------------*/

$entities:IFacadeMethods(); separator="\n\n"$
}>>