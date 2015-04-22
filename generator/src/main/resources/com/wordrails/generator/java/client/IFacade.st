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
		
	@GET("/posts/{stationId}/allUnread")
	ContentResponse<List<PostView\>\> getAllUnreadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId,
		@Query("page") int page, @Query("size") int size);
	
	@GET("/posts/{stationId}/allUnread")
	void getAllUnreadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId,
		@Query("page") int page, @Query("size") int size, Callback<ContentResponse<List<PostView\>\>\> _callback);
		
	@GET("/posts/{stationId}/unread")
	ContentResponse<List<PostView\>\> getUnreadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId);
	
	@GET("/posts/{stationId}/unread")
	void getUnreadPosts(@Path("stationId")Integer stationId, @Query("personId") Integer personId, Callback<ContentResponse<List<PostView\>\>\> _callback);
	
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
	
	@PUT("/persons/me/password")
	@FormUrlEncoded
	Response putRegId(@Field("regId") String regId);
	
	@PUT("/persons/me/password")
	@FormUrlEncoded
	void putRegId(@Field("regId") String regId, Callback<Response> _callback);
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

$entities:IFacadeMethods(); separator="\n\n"$
}>>