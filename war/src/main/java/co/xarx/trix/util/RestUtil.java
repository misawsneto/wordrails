package co.xarx.trix.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class RestUtil {

	public static <T> Page<T> getPageData(ImmutablePage<T> page, List<String> order) {
		if(page == null || page.isEmpty()) {
			return new PageImpl<>(new ArrayList<>());
		}

		return new PageImpl<>(page.items(), getPageable(page.getIndex(), page.size(), order), page.totalSize());
	}


	public static Pageable getPageable(Integer page, Integer size, List<String> order) {
		Assert.notNull(page, "Page must not be null");
		Assert.notNull(size, "Size must not be null");

		Sort sort = getSort(order);

		return new PageRequest(page, size, sort);
	}

	public static Sort getSort(List<String> order) {
		Sort sort = null;

		if (order != null && !order.isEmpty()) {
			List<Sort.Order> orders = new ArrayList<>();
			for (String s : order) {
				Sort.Direction d = Sort.Direction.ASC;

				if (s.charAt(0) == '-') {
					d = Sort.Direction.DESC;
					s = s.substring(1, s.length());
				}
				orders.add(new Sort.Order(d, s));
			}

			sort = new Sort(orders);
		}

		return sort;
	}


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