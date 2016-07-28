package co.xarx.trix.web.rest;

import co.xarx.trix.annotation.TimeIt;
import co.xarx.trix.api.v2.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
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

	protected FileItem getFileFromRequest() throws FileUploadException {
		ServletContext context = request.getServletContext();
		java.io.File repository = (java.io.File) context.getAttribute(ServletContext.TEMPDIR);
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		if (items != null && !items.isEmpty()) {
			return items.get(0);
		}

		return null;
	}


	@TimeIt
	protected void removeNotEmbeddedData(List<String> embeds, List objects, Class clazz, Set<String> postEmbeds) {
		for (String embed : postEmbeds) {
			if (!embeds.contains(embed)) {
				for (Object object : objects) {
					try {
						Field field = clazz.getDeclaredField(embed);
						field.setAccessible(true);
						field.set(object, null);
					} catch (NoSuchFieldException | IllegalAccessException e) {
						log.error("error to set data to null on embed", e);
					}
				}
			}
		}
	}
}
