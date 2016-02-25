package co.xarx.trix.web.rest;

import co.xarx.trix.PermissionId;
import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.StationsPermissions;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.PersonService;
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
import java.util.concurrent.ExecutionException;

@Path("/bookmarks")
@Consumes(MediaType.WILDCARD)
@Component
public class BookmarksResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private ESPostService esPostService;
	@Autowired
	private TrixAuthenticationProvider authProvider;

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

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		PermissionId pId = new PermissionId();
		pId.baseUrl = baseUrl;
		pId.networkId = network.id;
		pId.personId = person.id;

		List<Integer> readableIds = new ArrayList<>();
		try {
			StationsPermissions permissions = wordrailsService.getPersonPermissions(pId);
			readableIds = wordrailsService.getReadableStationIds(permissions);
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		BoolQueryBuilder mainQuery = esPostService.getBoolQueryBuilder(q, person.getId(), Constants.Post.STATE_PUBLISHED, readableIds, person.bookmarkPosts);

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
		if (person == null || "wordrails".equals(person.username)) throw new UnauthorizedException();

		bookmarkInserted.response = personService.toggleBookmark(person, postId);

		return bookmarkInserted;
	}

}