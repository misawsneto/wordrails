package co.xarx.trix.web.rest;

import co.xarx.trix.PermissionId;
import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.StationsPermissions;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.BookmarkRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.persistence.elasticsearch.BookmarkEsRespository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Path("/bookmarks")
@Consumes(MediaType.WILDCARD)
@Component
public class BookmarksResource {

	@Context
	private HttpServletRequest request;
	@Context
	private UriInfo uriInfo;
	@Context
	private HttpServletResponse response;
	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private BookmarkRepository bookmarkRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private BookmarkEsRespository bookmarkEsRespository;

	@GET
	@Path("/searchBookmarks")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<JSONObject>> searchBookmarks(@QueryParam("query") String q,
	                                                         @QueryParam("page") Integer page,
	                                                         @QueryParam("size") Integer size){
		Person person = authProvider.getLoggedPerson();
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		PermissionId pId = new PermissionId();
		pId.baseUrl = baseUrl;
		pId.networkId = network.id;
		pId.personId = person.id;

		StationsPermissions permissions = new StationsPermissions();
		try {
			permissions = wordrailsService.getPersonPermissions(pId);
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		List<Integer> readableIds = wordrailsService.getReadableStationIds(permissions);

		BoolQueryBuilder mainQuery = boolQuery();
		MatchQueryBuilder personQuery = matchQuery("bookmark.person.id", person.id);
		BoolQueryBuilder stationsQuery = boolQuery();
		for( Integer id: readableIds){
			stationsQuery.should(matchQuery("bookmark.post.stationId", id));
		}

		if(q == null || q.trim().isEmpty()){
			ContentResponse<List<JSONObject>> response = new ContentResponse<>();

			mainQuery = mainQuery.must(personQuery).must(stationsQuery);

			SearchResponse searchResponse = bookmarkEsRespository.runQuery(mainQuery.toString(), null, size, page);

			List<JSONObject> bookmarks = new ArrayList<>();

			for(SearchHit hit: searchResponse.getHits().getHits()){
				bookmarks.add(bookmarkEsRespository
						.convertToPostView(hit.getSourceAsString()));
			}

			response.content = bookmarks;
			return response;
		}

		MultiMatchQueryBuilder textQuery;

		try {
			textQuery = multiMatchQuery(q)
					.field("bookmark.post.title", 5)
					.field("bookmark.post.body", 2)
					.field("bookmark.post.topper")
					.field("bookmark.post.subheading")
					.field("bookmark.post.author.name")
					.field("bookmark.post.terms.name")
					.prefixLength(1)
					.fuzziness(Fuzziness.AUTO);
		} catch (Exception e){
			e.printStackTrace();

			ContentResponse<List<JSONObject>> response = new ContentResponse<>();
			response.content = new ArrayList<>();

			return response;
		}

		mainQuery = mainQuery
				.must(textQuery)
				.must(personQuery)
				.must(stationsQuery);

		//Sort not defined

		SearchResponse searchResponse = bookmarkEsRespository.runQuery(mainQuery.toString(), null, size, page);
		List<JSONObject> bookmarks = new ArrayList<>();

		for(SearchHit hit: searchResponse.getHits().getHits()){
			bookmarks.add(bookmarkEsRespository
					.convertToPostView(hit.getSourceAsString()));
		}

		ContentResponse<List<JSONObject>> response = new ContentResponse<>();
		response.content = bookmarks;

		return response;
	}

	@PUT
	@Path("/toggleBookmark")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public BooleanResponse toggleBookmark(@FormParam("postId") Integer postId){
		
		Person person = authProvider.getLoggedPerson();
		if(person == null || person.username.equals("wordrails"))
			throw new UnauthorizedException();
		
		Bookmark bookmark = new Bookmark();
		bookmark.post = postRepository.findOne(postId);
		bookmark.person = person;
		try{
			bookmarkRepository.save(bookmark);
			BooleanResponse content = new BooleanResponse();
			content.response = true;
			queryPersistence.incrementBookmarksCount(postId);
			return content;
		}catch(Exception e){
			queryPersistence.decrementBookmarksCount(postId);
			BooleanResponse content = new BooleanResponse();
			content.response = false;
			queryPersistence.deleteBookmark(postId, person.id);
			return content;
		}
	}
	
}