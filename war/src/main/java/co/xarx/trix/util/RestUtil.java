package co.xarx.trix.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.http.HttpStatus;

public class RestUtil {

	public static boolean isError(HttpStatus status) {
		HttpStatus.Series series = status.series();
		return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
	}

	public static String prettifyJSON(String json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);

		return gson.toJson(je);
	}

	public String getDeviceFromUserAgent(String userAgent) {
		UserAgent ua = UserAgent.parseUserAgentString(userAgent);
		return ua.getBrowser().getName();
	}
}