AuthenticationRequestInterceptor(package) ::=

<<package $package$;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import retrofit.RequestInterceptor;

class AuthenticationRequestInterceptor implements RequestInterceptor {
	IConnectivityManager manager;
	String url;
	String cookie;
	boolean isSocialLogin;
	
	// Traditional Login
	String username;
	String password;
	
	// Social Login
	String provider;
	String userId;
	String accessToken;
	
	@Override
	public synchronized void intercept(RequestFacade request) {
		request.addHeader("User-Agent", "OkHttp");
		request.addHeader("x-username", username != null && !username.equals("wordrails") ? username : "*");
		if (cookie != null) {
			request.addHeader("Cookie", "JSESSIONID=" + cookie);
		}
		if (manager.isConnectedOrConnecting()) {
			int maxAge = 0;
			request.addHeader("Cache-Control", "public, max-age=" + maxAge);
		} else {
			int maxStale = Integer.MAX_VALUE;
			request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
		}
	}

	synchronized void setUsername(String username) {
		this.username = username;
	}
	
	synchronized void logOut() {
		HttpGet request = new HttpGet(url + "/j_spring_security_logout");
		BasicHttpContext context = new BasicHttpContext();
		RequestConfig requestConfig = RequestConfig.custom().build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				
		try {
			client.execute(request, context).getEntity().getContent().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		cookie = null;
	}
	
	synchronized void logIn() {
		List<NameValuePair\> form = new ArrayList<NameValuePair\>(2);
		
		if(isSocialLogin){
			form = new ArrayList<NameValuePair\>(3);
			form.add(new BasicNameValuePair("provider", provider));
			form.add(new BasicNameValuePair("userId", userId));
			form.add(new BasicNameValuePair("accessToken", accessToken));
		} else {
			form = new ArrayList<NameValuePair\>(2);
			form.add(new BasicNameValuePair("username", username));
			form.add(new BasicNameValuePair("password", password));
		}

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(form, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	
		String requestEndpoint = isSocialLogin ? "/api/auth/signin" : "/api/persons/login";
		HttpPost request = new HttpPost(url + requestEndpoint);
		request.setEntity(entity);
		
		BasicCookieStore store = new BasicCookieStore();
	
		BasicHttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, store);		
	
		RequestConfig requestConfig = RequestConfig.custom().build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

		try {
			client.execute(request, context).getEntity().getContent().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		for (Iterator<Cookie\> i = store.getCookies().iterator(); i.hasNext(); ) {
			Cookie cookie = i.next(); 
			String name = cookie.getName();
			if ("JSESSIONID".equals(name)) {
				this.cookie = cookie.getValue();
			}
		}
	}	
}>>