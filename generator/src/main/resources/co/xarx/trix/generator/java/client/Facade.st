Facade(package, facade, entities) ::=

<<package $package$;

import android.content.Context;

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
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.api.v2.PersonalNotificationData;
import co.xarx.trix.api.v2.response.FileUpload;
import co.xarx.trix.api.v2.response.FileUpload;
import co.xarx.trix.api.v2.request.MessageFCM;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.api.RowView;

public class $facade$ {
	private String _url;
	private I$facade$ _facade;
	AuthenticationRequestInterceptor _interceptor;
	
	public $facade$(Context _context, 
					 IConnectivityManager _manager,
					 File _cacheDirectory, 
					 long _cacheMaxSize,
					 String _url, 
					 String _username, 
					 String _password, 
					 LogLevel _level) throws IOException {
		_interceptor = new AuthenticationRequestInterceptor();
		_interceptor.context = _context;
		_interceptor.manager = _manager;
		_interceptor.baseUrl = _url + "/api";
		_interceptor.username = _username;
		_interceptor.password = _password;
		_interceptor.isSocialLogin = false;
		
		initWordRails(_manager, _cacheDirectory, _cacheMaxSize, _url, _level);
	}
	
	public $facade$(Context _context, 
					 IConnectivityManager _manager,
					 File _cacheDirectory, 
					 long _cacheMaxSize,
					 String _url, 
					 String _provider, 
					 String _userId, 
					 String _accessToken, 
					 LogLevel _level) throws IOException {
		_interceptor = new AuthenticationRequestInterceptor();
		_interceptor.context = _context;
		_interceptor.manager = _manager;
		_interceptor.baseUrl = _url + "/api";
		_interceptor.provider = _provider;
		_interceptor.userId = _userId;
		_interceptor.accessToken = _accessToken;
		_interceptor.isSocialLogin = true;
		
		initWordRails(_manager, _cacheDirectory, _cacheMaxSize, _url, _level);
	}
	
	private void initWordRails(final IConnectivityManager _manager,
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
		_okHttpClient.setReadTimeout(1, TimeUnit.MINUTES);
		_okHttpClient.setWriteTimeout(1, TimeUnit.MINUTES);
		_okHttpClient.setConnectTimeout(1, TimeUnit.MINUTES);
		_okHttpClient.networkInterceptors().add(new Interceptor() {
			@Override
			public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
				com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
				String cacheControl = originalResponse.header("Cache-Control");
				if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
						cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
					return originalResponse.newBuilder()
							.removeHeader("Pragma")
							.header("Cache-Control", "public, max-age=" + Integer.MAX_VALUE)
							.build();
				} else {
					return originalResponse;
				}
			}
		});
		_okHttpClient.interceptors().add(new Interceptor() {
			@Override
			public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				if (!_manager.isConnectedOrConnecting()) {
					request = request.newBuilder()
							.removeHeader("Pragma")
							.header("Cache-Control", "public, only-if-cached")
							.build();
				}
				return chain.proceed(request);
			}
		});
		
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

	public List<TermView> getAllTerms(Integer taxonomyId, Integer perspectiveId, Integer stationId){
		return _facade.getAllTerms(taxonomyId, perspectiveId, stationId).content;
	}

	public void getAllTerms(Integer taxonomyId, Integer perspectiveId, Integer stationId, final Callback<List<TermView\>\> _callback){
		_facade.getAllTerms(taxonomyId, perspectiveId, stationId, new Callback<ContentResponse<List<TermView\>\>\>() {
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

	public List<PostView\> searchRecommends(Integer userId, String query, int page, int size){
		return _facade.searchRecommends(userId, query, page, size).content;
	}

	public void searchRecommends(Integer userId, String query, int page, int size, final Callback<List<PostView\>\> _callback){
		_facade.searchRecommends(userId, query, page, size, new Callback<ContentResponse<List<PostView\>\>\>() {
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
	public TermPerspectiveView getTermPerspectiveView(Integer termPerspectiveId, Integer termId, Integer stationPerspectiveId, int page, int size, boolean withBody, Integer stationId ){
		return _facade.getTermPerspectiveView(termPerspectiveId, termId, stationPerspectiveId, page, size, withBody, stationId);
	}

	public void getTermPerspectiveView(Integer termPerspectiveId, Integer termId, Integer stationPerspectiveId, int page, int size, boolean withBody, Integer stationId, Callback<TermPerspectiveView> _callback){
		_facade.getTermPerspectiveView(termPerspectiveId, termId, stationPerspectiveId, page, size, withBody, stationId, _callback);
	}

	public TermPerspectiveView getTermPerspectiveDefinition(Integer id){
		return _facade.getTermPerspectiveDefinition(id);
	}

	public void getTermPerspectiveDefinition(Integer id, Callback<TermPerspectiveView> _callback){
		_facade.getTermPerspectiveDefinition(id, _callback);
	}

	public RowView getRowView(Integer stationPerspectiveId, Integer termPerspectiveId, Integer childTermId, Integer stationId, int page, int size){
		return _facade.getRowView(stationPerspectiveId, termPerspectiveId, childTermId, stationId, page, size);
	}

	public void getRowView(Integer stationPerspectiveId, Integer termPerspectiveId, Integer childTermId, Integer stationId, int page, int size, Callback<RowView> _callback){
		_facade.getRowView(stationPerspectiveId, termPerspectiveId, childTermId, stationId, page, size, _callback);
	}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	public Response putPassword(String oldPassword, String newPassword){
		return _facade.putPassword(oldPassword, newPassword);
	}

	public void putPassword(String oldPassword, String newPassword, Callback<Response> _callback){
		_facade.putPassword(oldPassword, newPassword, _callback);
	}

	public Response postPasswordRecovery(PasswordRecoveryDto passwordRecoveryDto){
		return _facade.postPasswordRecovery(passwordRecoveryDto);
	}

	public void postPasswordRecovery(PasswordRecoveryDto passwordRecoveryDto, Callback<Response> _callback){
		_facade.postPasswordRecovery(passwordRecoveryDto, _callback);
	}

	public PostView getPostViewBySlug(String slug, Boolean withBody, Integer stationId){
    	return _facade.getPostViewBySlug(slug, withBody, stationId);
	}

			public PostDto findBySlug(String slug, Integer stationId) {
        		List<PostDto> posts = _facade.findBySlug(slug, stationId).content;
        		if (posts.isEmpty()) {
        			return null;
        		} else {
        			return posts.get(0);
        		}
        	}

        	public void findBySlug(String slug, Integer stationId, final Callback<PostDto> _callback) {
        		_facade.findBySlug(slug, stationId, new Callback<ContentResponse<List<PostDto>\>\>() {
        			@Override
        			public void success(ContentResponse<List<PostDto>\> _t, Response _response) {
        				List<PostDto> posts = _t.content;
        				if (posts.isEmpty()) {
        					_callback.success(null, _response);
        				} else {
        					_callback.success(posts.get(0), _response);
        				}
        			}

        			@Override
        			public void failure(RetrofitError _error) {
        				_callback.failure(_error);
        			}
        		});
        	}

            public PostProjectionDto findBySlug_PostProjection(String slug, Integer stationId) {
                List<PostProjectionDto> posts = _facade.findBySlug_PostProjection(slug, stationId, "postProjection").content;
                if (posts.isEmpty()) {
                    return null;
                } else {
                    return posts.get(0);
                }
            }

            public void findBySlug_PostProjection(String slug, Integer stationId, final Callback<PostProjectionDto> _callback) {
                _facade.findBySlug_PostProjection(slug, "postProjection", stationId, new Callback<ContentResponse<List<PostProjectionDto>\>\>() {
                    @Override
                    public void success(ContentResponse<List<PostProjectionDto>\> _t, Response _response) {
                        List<PostProjectionDto> posts = _t.content;
                        if (posts.isEmpty()) {
                            _callback.success(null, _response);
                        } else {
                            _callback.success(posts.get(0), _response);
                        }
                    }

                    @Override
                    public void failure(RetrofitError _error) {
                        _callback.failure(_error);
                    }
                });
            }

            public PostDto getPost(java.lang.Integer id, Integer stationId) {
                return _facade.getPost(id, stationId);
            }

            public void getPost(java.lang.Integer id, Integer stationId, Callback<PostDto> _callback) {
                _facade.getPost(id, stationId, _callback);
            }

            public java.util.List<TermDto> getPostTerms(java.lang.Integer id, java.lang.Integer stationId) {
                return _facade.getPostTerms(id, stationId).content;
            }

            public void getPostTerms(java.lang.Integer id, java.lang.Integer stationId, final Callback<java.util.List<TermDto>\> _callback) {
                _facade.getPostTerms(id, stationId, new Callback<ContentResponse<java.util.List<TermDto>\>\>() {
                    @Override
                    public void success(ContentResponse<java.util.List<TermDto>\> _t, Response _response) {
                        _callback.success(_t.content, _response);
                    }

                    @Override
                    public void failure(RetrofitError _error) {
                        _callback.failure(_error);
                    }
                });
            }

            public java.lang.Integer postPost(PostDto post, Integer stationId) {
                Response _response = _facade.postPost(post, stationId);
                int _status = _response.getStatus();
                if (_status != 201) {
                    throw new RuntimeException();
                }
                for (Header _header : _response.getHeaders()) {
                    String _name = _header.getName();
                    if ("Location".equals(_name)) {
                        String _value = _header.getValue();
                        post.setSelf(_value);
                        int _index = _value.lastIndexOf('/');
                        String _suffix = _value.substring(_index + 1);
                        java.lang.Integer id = new java.lang.Integer(_suffix);
                        post.id = id;
                        return id;
                    }
                }
                throw new RuntimeException();
            }

            public void postPost(PostDto post, Integer stationId, final Callback<java.lang.Integer> _callback) {
                _facade.postPost(post, stationId, new Callback<Void>() {
                    @Override
                    public void success(Void _t, Response _response) {
                        int _status = _response.getStatus();
                        if (_status != 201) {
                            RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
                            _callback.failure(_error);
                        }
                        for (Header _header : _response.getHeaders()) {
                            String _name = _header.getName();
                            if ("Location".equals(_name)) {
                                String _value = _header.getValue();
                                int _index = _value.lastIndexOf('/');
                                String _suffix = _value.substring(_index + 1);
                                java.lang.Integer id = new java.lang.Integer(_suffix);
                                _callback.success(id, _response);
                                return;
                            }
                        }
                        RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
                        _callback.failure(_error);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        _callback.failure(error);
                    }
                });
            }

            public CommentDto postComment(Integer postId, CommentDto comment, Integer stationId) {
                return _facade.postComment(postId, comment, stationId);
            }

            public void postComment(Integer postId, CommentDto comment, Integer stationId, final Callback<CommentDto> _callback) {
                _facade.postComment(postId, comment, stationId, _callback);
            }

            public void putPostTerms(java.lang.Integer id, List<String> terms, Integer stationId) {
                StringBuilder _uris = new StringBuilder();
                for (String _uri : terms) {
                    _uris.append(_uri);
                    _uris.append('\n');
                }
                Response _response = _facade.putPostTerms(id, new TypedString(_uris.toString()), stationId);
                int status = _response.getStatus();
                if (status != 204) {
                    throw new RuntimeException();
                }
            }

            public void putPostTerms(java.lang.Integer id, List<String> terms, Integer stationId, final Callback<Void> _callback) {
                StringBuilder _uris = new StringBuilder();
                for (String _uri : terms) {
                    _uris.append(_uri);
                    _uris.append('\n');
                }
                _facade.putPostTerms(id, new TypedString(_uris.toString()), stationId, new Callback<Void>() {
                    @Override
                    public void success(Void _t, Response _response) {
                        int _status = _response.getStatus();
                        if (_status != 204) {
                            RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
                            _callback.failure(_error);
                            return;
                        }
                        _callback.success(null, _response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        _callback.failure(error);
                    }
                });
            }

            public void putPostFeaturedImage(java.lang.Integer id, String featuredImage, Integer stationId) {
                Response _response;
                if (featuredImage == null) {
                    _response = _facade.deletePostFeaturedImage(id, stationId);
                } else {
                    _response = _facade.putPostFeaturedImage(id, new TypedString(featuredImage), stationId);
                }
                int _status = _response.getStatus();
                if (_status != 204) {
                    throw new RuntimeException();
                }
            }

            public void putPostFeaturedImage(java.lang.Integer id, String featuredImage, Integer stationId, final Callback<Void> _callback) {
                Callback<Void> _callback2 = new Callback<Void>() {
                    @Override
                    public void success(Void _t, Response _response) {
                        int _status = _response.getStatus();
                        if (_status != 204) {
                            RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
                            _callback.failure(_error);
                            return;
                        }
                        _callback.success(null, _response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        _callback.failure(error);
                    }
                };
                if (featuredImage == null) {
                    _facade.deletePostFeaturedImage(id, stationId, _callback2);
                } else {
                    _facade.putPostFeaturedImage(id, new TypedString(featuredImage), stationId, _callback2);
                }
            }

            public void patchPostTerms(java.lang.Integer id, List<String> terms, Integer stationId) {
                StringBuilder _uris = new StringBuilder();
                for (String _uri : terms) {
                    _uris.append(_uri);
                    _uris.append('\n');
                }
                Response _response = _facade.patchPostTerms(id, new TypedString(_uris.toString()), stationId);
                int status = _response.getStatus();
                if (status != 204) {
                    throw new RuntimeException();
                }
            }

            public void patchPostTerms(java.lang.Integer id, List<String> terms, Integer stationId, final Callback<Void> _callback) {
                StringBuilder _uris = new StringBuilder();
                for (String _uri : terms) {
                    _uris.append(_uri);
                    _uris.append('\n');
                }
                _facade.patchPostTerms(id, new TypedString(_uris.toString()), stationId, new Callback<Void>() {
                    @Override
                    public void success(Void _t, Response _response) {
                        int _status = _response.getStatus();
                        if (_status != 204) {
                            RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
                            _callback.failure(_error);
                            return;
                        }
                        _callback.success(null, _response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        _callback.failure(error);
                    }
                });
            }

            public void deletePost(java.lang.Integer id, Integer stationId) {
                Response _response = _facade.deletePost(id, stationId);
                int _status = _response.getStatus();
                if (_status != 204) {
                    throw new RuntimeException();
                }
            }

            public void deletePost(java.lang.Integer id, Integer stationId, final Callback<Void> _callback) {
                _facade.deletePost(id, stationId, new Callback<Void>() {
                    @Override
                    public void success(Void _t, Response _response) {
                        int _status = _response.getStatus();
                        if (_status != 204) {
                            RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
                            _callback.failure(_error);
                            return;
                        }
                        _callback.success(null, _response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        _callback.failure(error);
                    }
                });
            }

	public void getPostViewBySlug(String slug, Boolean withBody, Integer stationId, Callback<PostView> _callback){
		_facade.getPostViewBySlug(slug, withBody, stationId, _callback);
	}

	public PostView getPostViewById(Integer postId, Boolean withBody, Integer stationId){
		return _facade.getPostViewById(postId, withBody, stationId);
	}
	public void getPostViewById(Integer postId, Boolean withBody, Integer stationId, Callback<PostView> _callback){
		_facade.getPostViewById(postId, withBody, stationId, _callback);
	}

	public StringResponse getPostBody(Integer postId, Integer stationId){
		return _facade.getPostBody(postId, stationId);
	}

	public void getPostBody(Integer postId, Integer stationId, Callback<StringResponse> _callback){
		_facade.getPostBody(postId, stationId, _callback);
	}

	public BooleanResponse toggleBookmark(Integer postId, Integer stationId){
		return _facade.toggleBookmark(postId, stationId, "");
	}
	
	public void toggleBookmark(Integer postId, Integer stationId, Callback<BooleanResponse> _callback){
		_facade.toggleBookmark(postId, stationId, "", _callback);
	}
	
	public BooleanResponse toggleRecommend(Integer postId, Integer stationId){
		return _facade.toggleRecommend(postId, stationId, "");
	}
	
	public void toggleRecommend(Integer postId, Integer stationId, Callback<BooleanResponse> _callback){
		_facade.toggleRecommend(postId, stationId, "", _callback);
	}
	
	public Response putRegId(String regId, Integer networkId, Double lat, Double lng){
		return _facade.putRegId(regId, networkId, lat, lng);
	}
	
	public void putRegId(String regId, Integer networkId, Double lat, Double lng, Callback<Response> _callback){
		_facade.putRegId(regId, networkId, lat, lng, _callback);
	}

	public Response putLocation(String deviceCode, String deviceName, String device, Double lat, Double lng, Boolean fcm){
		return _facade.putLocation(deviceCode, deviceName, device, lat, lng, fcm);
	}

	public void putLocation(String deviceCode, String deviceName, String device, Double lat, Double lng, Boolean fcm, Callback<Response> _callback){
		_facade.putLocation(deviceCode, deviceName, device, lat, lng, fcm, _callback);
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

	public PersonDto findByUsername(String username) {
        PersonDto person = _facade.findByUsername(username);
        return person;
    }

	public void findByUsername(String username, Callback<PersonDto\> _callback) {
        _facade.findByUsername(username, _callback);
    }

    public List<NotificationData\> getNotifications(Integer page, Integer size) {
        return _facade.getNotifications(page, size);
    }

    public void getNotifications(Integer page, Integer size, Callback<List<NotificationData\>\> _callback) {
        _facade.getNotifications(page, size, _callback);
    }

	public PersonDto postCreatePerson(PersonCreateDto personCreate){
		return _facade.postCreatePerson(personCreate);
	}
	
	public void postCreatePerson(PersonCreateDto personCreate, Callback<PersonDto> _callback){
		_facade.postCreatePerson(personCreate, _callback);
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

    public List<CommentDto> findPostCommentsOrderByDate(Integer postId, Integer stationId, Integer page, Integer size, List<String> sort) {
    	return _facade.findPostCommentsOrderByDate(postId, stationId, page, size, sort).content;
    }

    public void findPostCommentsOrderByDate(Integer postId, Integer stationId, Integer page, Integer size, List<String> sort, final Callback<List<CommentDto\>\> _callback) {
    	_facade.findPostCommentsOrderByDate(postId, stationId, page, size, sort, new Callback<ContentResponse<List<CommentDto\>\>\>() {
    		@Override
    		public void success(ContentResponse<List<CommentDto\>\> _t, Response _response) {
    			_callback.success(_t.content, _response);
    		}

    		@Override
    		public void failure(RetrofitError _error) {
    			_callback.failure(_error);
    		}
    	});
    }

    public List<CommentProjectionDto> findPostCommentsOrderByDate_CommentProjection(Integer postId, Integer stationId, Integer page, Integer size, List<String> sort) {
    	return _facade.findPostCommentsOrderByDate_CommentProjection(postId, stationId, page, size, sort, "commentProjection").content;
    }

    public void findPostCommentsOrderByDate_CommentProjection(Integer postId, Integer stationId, Integer page, Integer size, List<String> sort, final Callback<List<CommentProjectionDto\>\> _callback) {
    	_facade.findPostCommentsOrderByDate_CommentProjection(postId, stationId, page, size, sort, "commentProjection", new Callback<ContentResponse<List<CommentProjectionDto\>\>\>() {
    		@Override
    		public void success(ContentResponse<List<CommentProjectionDto\>\> _t, Response _response) {
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

	public Response putPostSeen(Integer postId, Integer timeReading, Long date, Integer stationId) {
        return _facade.putPostSeen(postId, timeReading, date, stationId, "");
    }

    public void putPostSeen(Integer postId, Integer timeReading, Long date, Integer stationId, Callback<Response> _callback) {
    	_facade.putPostSeen(postId, timeReading, date, stationId, "", _callback);
    }

	public Response putNotificationSeen(MessageFCM messageFcm) {
        return _facade.putNotificationSeen(messageFcm);
    }

    public void putNotificationSeen(MessageFCM messageFcm, Callback<Response> _callback) {
    	_facade.putNotificationSeen(messageFcm, _callback);
    }

	public Response putTermSeen(Integer termId, Integer timeReading, Long date, Integer stationId) {
        return _facade.putTermSeen(termId, timeReading, date, stationId, "");
    }

    public void putTermSeen(Integer termId, Integer timeReading, Long date, Integer stationId, Callback<Response> _callback) {
    	_facade.putTermSeen(termId, timeReading, date, stationId, "", _callback);
    }

/*---------------------------------------------------------------------------*/
	
	public List<StationView\> putFollowingStations(List<Integer\> stationIds){
		return _facade.putFollowingStations(stationIds);
	}
	
	public void putFollowingStations(List<Integer\> stationIds, Callback<List<StationView\>\> _callback){
		_facade.putFollowingStations(stationIds, _callback);
	}

/*---------------------------------------------------------------------------*/

$entities:FacadeMethods(); separator="\n\n"$
}>>