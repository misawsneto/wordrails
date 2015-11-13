package com.wordrails.domain;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

public class ServiceGenerator {

	// No need to instantiate this class.
	private ServiceGenerator() {
	}

	public static <S> S createService(Class<S> serviceClass, String baseUrl) {
		RestAdapter.Builder builder = new RestAdapter.Builder()
				.setEndpoint(baseUrl);

		RestAdapter adapter = builder.build();

		return adapter.create(serviceClass);
	}

	public static <S> S createService(Class<S> serviceClass, String baseUrl, String username, String password) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);

		RestAdapter.Builder builder = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setConverter(new JacksonConverter(mapper))
				.setEndpoint(baseUrl);

		if (username != null && password != null) {
			final String credentials = username + ":" + password;

			builder.setRequestInterceptor(new RequestInterceptor() {
				@Override
				public void intercept(RequestFacade request) {
					String string = "Basic " + new String(Base64.encodeBase64(credentials.getBytes(Charset.forName("US-ASCII"))));
					request.addHeader("Accept", "application/json");
					request.addHeader("Authorization", string);
				}
			});
		}

		RestAdapter adapter = builder.build();

		return adapter.create(serviceClass);
	}
}
