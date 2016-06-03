package co.xarx.trix.web.rest;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.SearchView;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by misael on 03/06/2016.
 */
@Path("/v2")
@Consumes(MediaType.WILDCARD)
@Component
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class V2Resources {

	@Autowired
	PostsResource postsResource;

	@GET
	@Path("/posts/search")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> searchPosts(@Context HttpServletRequest request,
												   @QueryParam("q") String q,
												   @QueryParam("page") Integer page,
												   @QueryParam("size") Integer size,
												   @QueryParam("order") Integer order) {

		boolean sortByDate = "-date".equals(order) ? true : false;

		return postsResource.searchPosts(request,q,null,null,null,null,sortByDate,page,
				size);
	}
}
