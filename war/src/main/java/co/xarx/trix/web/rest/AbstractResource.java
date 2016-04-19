package co.xarx.trix.web.rest;

import co.xarx.trix.api.v2.ErrorData;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractResource {

	@Context
	public UriInfo uriInfo;
	@Context
	public HttpServletRequest request;
	@Context
	public HttpServletResponse response;

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

	protected Response unprocessableEntity(String message) {
		return Response.status(422).entity(new ErrorData(message)).build();
	}
}
