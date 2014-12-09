AuthenticationRequestInterceptor(package) ::=

<<package $package$;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import retrofit.RequestInterceptor;

class AuthenticationRequestInterceptor implements RequestInterceptor {
	IConnectivityManager manager;
	String url;
	String username;
	String password;
	String cookie;
	
	@Override
	public synchronized void intercept(RequestFacade request) {
		if (cookie != null) {
			request.addHeader("Cookie", "JSESSIONID=" + cookie);
		}
		if (manager.isConnectedOrConnecting()) {
            int maxAge = 0;
            request.addHeader("Cache-Control", "max-age=" + maxAge);
		} else {
			int maxStale = Integer.MAX_VALUE;
            request.addHeader("Cache-Control", "only-if-cached, max-stale=" + maxStale);				
		}
	}
	
	synchronized void logIn() {
		List<NameValuePair\> form = new ArrayList<NameValuePair\>(2);
		form.add(new BasicNameValuePair("j_username", username));
		form.add(new BasicNameValuePair("j_password", password));

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(form, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	
		HttpPost request = new HttpPost(url + "/j_spring_security_check");
		request.setEntity(entity);
		
		BasicCookieStore store = new BasicCookieStore();
	
		BasicHttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, store);		
	
		DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			client.execute(request, context).getEntity().consumeContent();
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