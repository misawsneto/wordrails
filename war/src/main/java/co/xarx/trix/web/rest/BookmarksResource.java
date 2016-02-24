package co.xarx.trix.web.rest;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.auth.StationPermissionService;
import co.xarx.trix.services.elasticsearch.ESPostService;
import co.xarx.trix.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/bookmarks")
@Consumes(MediaType.WILDCARD)
@Component
public class BookmarksResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private StationPermissionService stationPermissionService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private ESPostService esPostService;
	@Autowired
	private AuthService authProvider;

	@GET
	@Path("/searchBookmarks")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> searchBookmarks(@QueryParam("query") String q,
															 @QueryParam("page") Integer page,
															 @QueryParam("size") Integer size){
		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());
		if(CollectionUtils.isEmpty(person.getBookmarkPosts())) {
			ContentResponse<List<PostView>> response = new ContentResponse<>();
			response.content = new ArrayList<>();

			return response;
		}

		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithPermission();

		if (stationsWithPermission == null || stationsWithPermission.isEmpty()) {
			ContentResponse<List<PostView>> response = new ContentResponse<>();
			response.content = new ArrayList<>();

			return response;
		}

		BoolQueryBuilder mainQuery = esPostService.getBoolQueryBuilder(q, person.getId(),
				Constants.Post.STATE_PUBLISHED, stationsWithPermission, person.bookmarkPosts);

		Pageable pageable = new PageRequest(page, size);


		Pair<Integer, List<PostView>> postsViews = esPostService.searchIndex(mainQuery, pageable, null);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postsViews.getRight();

		return response;
	}

	@PUT
	@Path("/toggleBookmark")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public BooleanResponse toggleBookmark(@FormParam("postId") Integer postId){
		BooleanResponse bookmarkInserted = new BooleanResponse();

		Person person = authProvider.getLoggedPerson();
		person = personRepository.findOne(person.getId());
		if (person == null || person.username.equals("wordrails")) throw new UnauthorizedException();

		bookmarkInserted.response = personService.toggleBookmark(person, postId);

		return bookmarkInserted;
	}

}