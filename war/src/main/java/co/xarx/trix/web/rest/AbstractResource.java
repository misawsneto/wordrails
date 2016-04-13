package co.xarx.trix.web.rest;

import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractResource {

	@Context
	public UriInfo uriInfo;
	@Context
	public HttpServletRequest request;
	@Context
	public HttpServletResponse response;
	@Context
	public HttpRequest httpRequest;
	@Context
	public HttpResponse httpResponse;

	protected void forward() throws IOException {
		forward(uriInfo.getPath());
	}

	protected void forward(Map<String, String> queryParams) throws IOException {
		forward(uriInfo.getPath(), queryParams);
	}

	protected void forward(String uri) throws IOException {
		forward(uri, null);
	}

	protected void forward(String uri, Map<String, String> queryParams) throws IOException {
		String path = "/data" + uri;

		if(queryParams != null && queryParams.size() > 0) {
			path += "?";
			Iterator<String> it = queryParams.keySet().iterator();
			do {
				String key = it.next();
				path += key + "=" + queryParams.get(key);
				if(it.hasNext())
					path += "&";
			} while(it.hasNext());
		}


		ServletContext servletContext = request.getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException e) {
			throw new IOException(e);
		}
	}

	protected Pageable getPageable(Integer page, Integer size, List<String> order) {
		Assert.notNull(page, "Page must not be null");
		Assert.notNull(size, "Size must not be null");

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

		return new PageRequest(page, size, sort);
	}
}
