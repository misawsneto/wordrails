AuthenticationRequestInterceptor(package) ::=

<<package $package$;

import android.content.Context;
import android.preference.PreferenceManager;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.FormEncodingBuilder;

import java.net.CookieManager;
import java.net.HttpCookie;

import co.xarx.trix.api.IConnectivityManager;
import retrofit.RequestInterceptor;

class AuthenticationRequestInterceptor implements RequestInterceptor {

    private static final CookieManager COOKIE_MANAGER = new CookieManager();
    private static final OkHttpClient OK_HTTP = new OkHttpClient().setCookieHandler(COOKIE_MANAGER);
    private static final String PREF_TRIX_COOKIE = "TRIX_COOKIE";

	IConnectivityManager manager;
    Context context;

	String baseUrl;
	String cookie;
	boolean isSocialLogin;
	
	String username;
	String password;
	String provider;
	String userId;
	String accessToken;
	
	@Override
	public synchronized void intercept(RequestFacade request) {
		request.addHeader("User-Agent", "android2");
        request.addHeader("x-username", isAnonymous() ? "*" : username);

        if (cookie == null && context != null) {
            cookie = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(PREF_TRIX_COOKIE, null);
        }
        if(cookie != null) {
            request.addHeader("Cookie", "JSESSIONID=" + cookie);
        }

        if(manager.isConnectedOrConnecting()) {
            byte maxAge = 0;
            request.addHeader("Cache-Control", "public, max-age=" + maxAge);
        } else {
            int maxStale = Integer.MAX_VALUE;
            request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
        }
	}

	synchronized void setUsername(String username) {
		this.username = username;
	}
	
	synchronized void logIn() {
		if(isAnonymous()){
			return;
		}
		
        RequestBody body;
        String url = baseUrl + "/persons/login";
        if(isSocialLogin){
            body = new FormEncodingBuilder()
            			.add("username", username)
            			.add("social-userId", userId)
            			.add("social-token", accessToken)
            			.build();
        } else {
            body = new FormEncodingBuilder()
            			.add("username", username)
            			.add("password", password)
            			.build();
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        if(provider != null){
            requestBuilder.addHeader("provider", provider);
        }
        Request request = requestBuilder.build();
                
        try {
            Response response = OK_HTTP.newCall(request).execute();
            if(response.isSuccessful()) {
                for (HttpCookie cookie : COOKIE_MANAGER.getCookieStore().getCookies()) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        this.cookie = cookie.getValue();
                        if(context != null) {
                            PreferenceManager.getDefaultSharedPreferences(context)
                                    .edit()
                                    .putString(PREF_TRIX_COOKIE, this.cookie)
                                    .apply();
                        }
                        break;
                    }
                }
            }
            try {
                response.body().close();
            } catch(Exception e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
	}
	
	synchronized void logOut() {
		Request request = new Request.Builder()
                .url(baseUrl + "/j_spring_security_logout")
                .get()
                .build();
        try {
            OK_HTTP.newCall(request).execute();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            cookie = null;
            if(context != null) {
                PreferenceManager.getDefaultSharedPreferences(context)
                        .edit()
                        .remove(PREF_TRIX_COOKIE)
                        .apply();
            }
        }
	}

	synchronized private boolean isAnonymous(){
		return username == null || username.equals("wordrails");
	}
}>>