Facade(package, facade, entities) ::=

<<package $package$;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedString;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.api.v2.PersonalNotificationData;
import co.xarx.trix.api.v2.response.FileUpload;
import co.xarx.trix.api.v2.response.FileUpload;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.api.RowView;

public class $facade$ {
	private String _url;
	private I$facade$ _facade;
	AuthenticationRequestInterceptor _interceptor;
	
	public $facade$(IConnectivityManager _manager,
					 File _cacheDirectory, 
					 long _cacheMaxSize,
					 String _url, 
					 String _username, 
					 String _password, 
					 LogLevel _level) throws IOException {
		_interceptor = new AuthenticationRequestInterceptor();
		_interceptor.manager = _manager;
		_interceptor.url = _url;
		_interceptor.username = _username;
		_interceptor.password = _password;
		_interceptor.isSocialLogin = false;
		
		initWordRails(_manager, _cacheDirectory, _cacheMaxSize, _url, _level);
	}
	
	public $facade$(IConnectivityManager _manager,
					 File _cacheDirectory, 
					 long _cacheMaxSize,
					 String _url, 
					 String _provider, 
					 String _userId, 
					 String _accessToken, 
					 LogLevel _level) throws IOException {
		_interceptor = new AuthenticationRequestInterceptor();
		_interceptor.manager = _manager;
		_interceptor.url = _url;
		_interceptor.provider = _provider;
		_interceptor.userId = _userId;
		_interceptor.accessToken = _accessToken;
		_interceptor.isSocialLogin = true;
		
		initWordRails(_manager, _cacheDirectory, _cacheMaxSize, _url, _level);
	}
	
	private void initWordRails(IConnectivityManager _manager,
					 File _cacheDirectory, 
					 long _cacheMaxSize,
					 String _url, 
					 LogLevel _level) throws IOException {
		Gson _gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, new DateJsonSerializer())
    		.registerTypeAdapter(Date.class, new DateJsonDeserializer())
			.create();
		Converter _converter = new GsonConverter(_gson);

		OkHttpClient _okHttpClient = new OkHttpClient();
	    _okHttpClient.setConnectTimeout(10, TimeUnit.MINUTES);
	    _okHttpClient.setReadTimeout(10, TimeUnit.MINUTES);

		if(_cacheMaxSize > 0){
			Cache _cache = new Cache(_cacheDirectory, _cacheMaxSize);
			_okHttpClient.setCache(_cache);
		}

		Client _client = new OkClient(_okHttpClient);

		RestAdapter _builder = new RestAdapter.Builder()
			.setClient(_client)
			.setConverter(_converter)
			.setRequestInterceptor(_interceptor)
			.setEndpoint(_url + "/api")
			.build();
		_builder.setLogLevel(_level);
		ITrix target = _builder.create(ITrix.class);

		ClassLoader _loader = I$facade$.class.getClassLoader();

		Class<?\>[] _interfaces = {I$facade$.class};

		AuthenticationInvocationHandler handler = new AuthenticationInvocationHandler();
		handler.target = target;
		handler.interceptor = _interceptor;

		_facade = (I$facade$) Proxy.newProxyInstance(_loader, _interfaces, handler);
	}

	public Batch createBatch() {
		return new Batch(_facade);
	}

	public void setUsername(String username) {
		_interceptor.setUsername(username);
	}

	public void login(){
		_interceptor.logIn();
	}

	public void logout(){
		_interceptor.logOut();
	}

/*---------------------------------------------------------------------------*/
	$entities:{entity | $GetSelf(entity)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/

	public List<PostView> findPostsByTagAndStationId(String tagName, Integer stationId, int page, int size){
		return _facade.findPostsByTagAndStationId(tagName, stationId, page, size).content;
	}

	public void findPostsByTagAndStationId(String tagName, Integer stationId, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findPostsByTagAndStationId(tagName, stationId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> findPostsByTerm(Integer termId, int page, int size){
    		return _facade.findPostsByTerm(termId, page, size).content;
    	}

    public void findPostsByTerm(Integer termId, int page, int size, final Callback<List<PostView\>\> _callback){
        _facade.findPostsByTerm(termId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
            @Override
            public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
                _callback.success(_t.content, _response);
            }

            @Override
            public void failure(RetrofitError _error) {
                _callback.failure(_error);
            }
        });
    }

	public List<PostView> findStationPostsByTags(String tags, Integer stationId, int page, int size){
        return _facade.findStationPostsByTags(tags, stationId, page, size).content;
    }

    public void findStationPostsByTags(String tags, Integer stationId, int page, int size, final Callback<List<PostView\>\> _callback){
        _facade.findStationPostsByTags(tags, stationId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
            @Override
            public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
                _callback.success(_t.content, _response);
            }

            @Override
            public void failure(RetrofitError _error) {
                _callback.failure(_error);
            }
        });
    }

	public List<PostView> findPostsAndPostsPromotedByBody(Integer stationId, String body, int page, int size){
		return _facade.findPostsAndPostsPromotedByBody(stationId, body, page, size).content;
	}

	public void findPostsAndPostsPromotedByBody(Integer stationId, String body, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findPostsAndPostsPromotedByBody(stationId, body, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> findPostsAndPostsPromotedByTermId(Integer stationId, Integer termId, int page, int size){
		return _facade.findPostsAndPostsPromotedByTermId(stationId, termId, page, size).content;
	}

	public void findPostsAndPostsPromotedByTermId(Integer stationId, Integer termId, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findPostsAndPostsPromotedByTermId(stationId, termId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<TermDto> getTermTree(Integer perspectiveId){
		return _facade.getTermTree(perspectiveId).content;
	}

	public void getTermTree(Integer perspectiveId, final Callback<List<TermDto\>\> _callback){
		_facade.getTermTree(perspectiveId, new Callback<ContentResponse<List<TermDto\>\>\>() {
			@Override
			public void success(ContentResponse<List<TermDto\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<TermView> getAllTerms(Integer taxonomyId, Integer perspectiveId){
		return _facade.getAllTerms(taxonomyId, perspectiveId).content;
	}

	public void getAllTerms(Integer taxonomyId, Integer perspectiveId, final Callback<List<TermView\>\> _callback){
		_facade.getAllTerms(taxonomyId, perspectiveId, new Callback<ContentResponse<List<TermView\>\>\>() {
			@Override
			public void success(ContentResponse<List<TermView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> findRecentPosts(Integer stationId, int page, int size){
		return _facade.findRecentPosts(stationId, page, size).content;
	}

	public void findRecentPosts(Integer stationId, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findRecentPosts(stationId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> findPopularPosts(Integer stationId, int page, int size){
		return _facade.findPopularPosts(stationId, page, size).content;
	}

	public void findPopularPosts(Integer stationId, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findPopularPosts(stationId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> findPostsByStationIdAndAuthorIdAndState(Integer stationId, Integer authorId, String state, int page, int size){
		return _facade.findPostsByStationIdAndAuthorIdAndState(stationId, authorId, state, page, size).content;
	}

	public void findPostsByStationIdAndAuthorIdAndState(Integer stationId, Integer authorId, String state, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findPostsByStationIdAndAuthorIdAndState(stationId, authorId, state, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> findPostsAndPostsPromotedByAuthorId(Integer stationId, Integer authorId, int page, int size){
		return _facade.findPostsAndPostsPromotedByAuthorId(stationId, authorId, page, size).content;
	}

	public void findPostsAndPostsPromotedByAuthorId(Integer stationId, Integer authorId, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.findPostsAndPostsPromotedByAuthorId(stationId, authorId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public Response socialLogin(String provider, String userId, String accessToken){
		return _facade.socialLogin(provider, userId, accessToken);
	}

	public void socialLogin(String provider, String userId, String accessToken, Callback<Response> _callback){
		_facade.socialLogin(provider, userId, accessToken, _callback);
	}

	public PostView convertPost(Integer postId, String state){
		return _facade.convertPost(postId, state).content;
	}

	public void convertPost(Integer postId, String state, final Callback<PostView\> _callback){
		_facade.convertPost(postId, state, new Callback<ContentResponse<PostView\>\>() {
			@Override
			public void success(ContentResponse<PostView\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}
	
	public List<PostData\> searchPosts(String q, List<Integer\> aIds, List<Integer\> sIds, String st, String from,
                                  String until, List<Integer\> cIds, List<String\> tags, Integer page, Integer size,
                                  List<String\> orders, List<String\> embeds, String snippet){
        return _facade.searchPosts(q, aIds, sIds, st, from, until, cIds, tags, page, size, orders, embeds, snippet).content;
    }

    public void searchPosts(String q, List<Integer\> aIds, List<Integer\> sIds, String st, String from,
                            String until, List<Integer\> cIds, List<String\> tags, Integer page, Integer size,
                            List<String\> orders, List<String\> embeds, String snippet, final Callback<List<PostData\>\> _callback){
        _facade.searchPosts(q, aIds, sIds, st, from, until, cIds, tags, page, size, orders, embeds, snippet, new
        Callback<ContentResponse<List<PostData\>\>\>() {
            @Override
            public void success(ContentResponse<List<PostData\>\> _t, Response _response) {
                _callback.success(_t.content, _response);
            }

            @Override
            public void failure(RetrofitError _error) {
                _callback.failure(_error);
            }
        });
    }

	public List<PostView\> searchBookmarks(String query, int page, int size){
		return _facade.searchBookmarks(query, page, size).content;
	}

	public void searchBookmarks(String query, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.searchBookmarks(query, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView\> searchRecommends(String query, int page, int size){
		return _facade.searchRecommends(query, page, size).content;
	}

	public void searchRecommends(String query, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.searchRecommends(query, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PersonalNotificationData\> searchNotifications(String query, int page, int size){
		return _facade.searchNotifications(query, page, size).content;
	}

	public void searchNotifications(String query, int page, int size, final Callback<List<PersonalNotificationData\>\> _callback){
		_facade.searchNotifications(query, page, size, new Callback<ContentResponse<List<PersonalNotificationData\>\>\>() {
			@Override
			public void success(ContentResponse<List<PersonalNotificationData\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}


	public List<PostView> getPostReadPosts(Integer stationId, Integer personId){
		return _facade.getPostReadPosts(stationId, personId).content;
	}

	public void getPostReadPosts(Integer stationId, Integer personId, final Callback<List<PostView\>\> _callback){
		_facade.getPostReadPosts(stationId, personId, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

	public List<PostView> getAllPostReadPosts(Integer stationId, Integer personId, int page, int size){
		return _facade.getAllPostReadPosts(stationId, personId, page, size).content;
	}

	public void getAllPostReadPosts(Integer stationId, Integer personId, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.getAllPostReadPosts(stationId, personId, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
			@Override
			public void success(ContentResponse<List<PostView\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}
		});
	}

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public Response getFileContents(Integer id) {
		return _facade.getFileContents(id);
	}

	public void getFileContents(Integer id, Callback<Void> _callback) {
		_facade.getFileContents(id, _callback);
	}

	public Response putFileContents(Integer id, TypedInput contents) {
		return _facade.putFileContents(id, contents);
	}

	public void putFileContents(Integer id, TypedInput contents, Callback<Void> _callback) {
		_facade.putFileContents(id, contents, _callback);
	}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public TermPerspectiveView getTermPerspectiveView(Integer termPerspectiveId, Integer termId, Integer stationPerspectiveId, int page, int size){
		return _facade.getTermPerspectiveView(termPerspectiveId, termId, stationPerspectiveId, page, size);
	}

	public void getTermPerspectiveView(Integer termPerspectiveId, Integer termId, Integer stationPerspectiveId, int page, int size, Callback<TermPerspectiveView> _callback){
		_facade.getTermPerspectiveView(termPerspectiveId, termId, stationPerspectiveId, page, size, _callback);
	}

	public TermPerspectiveView getTermPerspectiveDefinition(Integer id){
		return _facade.getTermPerspectiveDefinition(id);
	}

	public void getTermPerspectiveDefinition(Integer id, Callback<TermPerspectiveView> _callback){
		_facade.getTermPerspectiveDefinition(id, _callback);
	}

	public RowView getRowView(Integer stationPerspectiveId, Integer termPerspectiveId, Integer childTermId, int page, int size){
		return _facade.getRowView(stationPerspectiveId, termPerspectiveId, childTermId, page, size);
	}

	public void getRowView(Integer stationPerspectiveId, Integer termPerspectiveId, Integer childTermId, int page, int size, Callback<RowView> _callback){
		_facade.getRowView(stationPerspectiveId, termPerspectiveId, childTermId, page, size, _callback);
	}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public Response putPassword(String oldPassword, String newPassword){
		return _facade.putPassword(oldPassword, newPassword);
	}

	public void putPassword(String oldPassword, String newPassword, Callback<Response> _callback){
		_facade.putPassword(oldPassword, newPassword, _callback);
	}

	public PostView getPostViewBySlug(String slug, Boolean withBody){
    	return _facade.getPostViewBySlug(slug, withBody);
	}

	public void getPostViewBySlug(String slug, Boolean withBody, Callback<PostView> _callback){
		_facade.getPostViewBySlug(slug, withBody, _callback);
	}

	public PostView getPostViewById(Integer postId, Boolean withBody){
		return _facade.getPostViewById(postId, withBody);
	}

	public void getPostViewById(Integer postId, Boolean withBody, Callback<PostView> _callback){
		_facade.getPostViewById(postId, withBody, _callback);
	}

	public StringResponse getPostBody(Integer postId){
		return _facade.getPostBody(postId);
	}

	public void getPostBody(Integer postId, Callback<StringResponse> _callback){
		_facade.getPostBody(postId, _callback);
	}

	public BooleanResponse toggleBookmark(Integer postId){
		return _facade.toggleBookmark(postId, "");
	}
	
	public void toggleBookmark(Integer postId, Callback<BooleanResponse> _callback){
		_facade.toggleBookmark(postId, "", _callback);
	}
	
	public BooleanResponse toggleRecommend(Integer postId){
		return _facade.toggleRecommend(postId, "");
	}
	
	public void toggleRecommend(Integer postId, Callback<BooleanResponse> _callback){
		_facade.toggleRecommend(postId, "", _callback);
	}
	
	public Response putRegId(String regId, Integer networkId, Double lat, Double lng){
		return _facade.putRegId(regId, networkId, lat, lng);
	}
	
	public void putRegId(String regId, Integer networkId, Double lat, Double lng, Callback<Response> _callback){
		_facade.putRegId(regId, networkId, lat, lng, _callback);
	}

	public Response putLocation(String deviceCode, String device, Double lat, Double lng){
		return _facade.putLocation(deviceCode, device, lat, lng);
	}

	public void putLocation(String deviceCode, String device, Double lat, Double lng, Callback<Response> _callback){
		_facade.putLocation(deviceCode, device, lat, lng, _callback);
	}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public PersonPermissions getNetworkPersonPermissions(Integer id){
		return _facade.getNetworkPersonPermissions(id);
	}
	
	public void getNetworkPersonPermissions(Integer id, Callback<PersonPermissions> _callback){
		_facade.getNetworkPersonPermissions(id, _callback);
	}
	
	public PersonData getInitialData(){
		return _facade.getInitialData();
	}
	
	public void getInitialData(Callback<PersonData> _callback){
		_facade.getInitialData(_callback);
	}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public Response getAmazonSignedUrl(String hash, String type){
		return _facade.getAmazonSignedUrl(hash, type);
	}
	
	public void getAmazonSignedUrl(String hash, String type, Callback<Response> _callback){
		_facade.getAmazonSignedUrl(hash, type, _callback);
	}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public ImageUploadResponse postImageUpload(String imageType, TypedInput contents){
		return _facade.postImageUpload(imageType, contents);
	}
	
	public void postImageUpload(String imageType, TypedInput contents, Callback<ImageUploadResponse> _callback){
		_facade.postImageUpload(imageType, contents, _callback);
	}

	public FileUpload postUploadVideo(TypedInput contents){
		return _facade.postUploadVideo(contents);
	}
	
	public void postUploadVideo(TypedInput contents, Callback<FileUpload> _callback){
		_facade.postUploadVideo(contents, _callback);
	}

	public FileUpload postUploadAudio(TypedInput contents){
		return _facade.postUploadAudio(contents);
	}
	
	public void postUploadAudio(TypedInput contents, Callback<FileUpload> _callback){
		_facade.postUploadAudio(contents, _callback);
	}

	public FileUpload postUploadDoc(TypedInput contents){
		return _facade.postUploadDoc(contents);
	}
	
	public void postUploadDoc(TypedInput contents, Callback<FileUpload> _callback){
		_facade.postUploadDoc(contents, _callback);
	}
/*---------------------------------------------------------------------------*/

    public CommentDto postComment(Integer postId, CommentDto comment) {
        return _facade.postComment(postId, comment);
    }

    public void postComment(Integer postId, CommentDto comment, final Callback<CommentDto> _callback) {
    	_facade.postComment(postId, comment, _callback);
    }

/*---------------------------------------------------------------------------*/

$entities:FacadeMethods(); separator="\n\n"$
}>>