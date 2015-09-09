package com.wordrails.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Post;
import com.wordrails.converter.PostConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.business.Term;
import com.wordrails.converter.TermConverter;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;

@Path("/terms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class TermResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired PersonRepository personRepository;

	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired
	TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired TermRepository termRepository;
	private @Autowired TermConverter termConverter;
	private @Autowired PostConverter postConverter;

	@GET
	@Path("/termTree")
	public Response getTermTree(@QueryParam("taxonomyId") Integer taxonomyId, @QueryParam("perspectiveId") Integer perspectiveId) throws JsonGenerationException, JsonMappingException, IOException {
		org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
		List<Term> allTerms;
		if(perspectiveId != null){
			allTerms = termRepository.findByPerspectiveId(perspectiveId);
		}else{
			allTerms = termRepository.findByTaxonomyId(taxonomyId);
		}

		List<Term> roots =  wordrailsService.createTermTree(allTerms);

		String json = mapper.writeValueAsString(roots);
		return Response.status(Status.OK).entity(json).build();
	}
	
	@GET
	@Path("/allTerms")
	public ContentResponse<List<TermView>> getAllTerms(@QueryParam("taxonomyId") Integer taxonomyId, @QueryParam("perspectiveId") Integer perspectiveId) throws JsonGenerationException, JsonMappingException, IOException {
		List<Term> allTerms;
		if(perspectiveId != null){
			allTerms = termRepository.findByPerspectiveId(perspectiveId);
		}else{
			allTerms = termRepository.findByTaxonomyId(taxonomyId);
		}

		ContentResponse<List<TermView>> response = new ContentResponse<List<TermView>>();
		response.content = termConverter.convertToViews(allTerms);
		return response;
	}

	@GET
	@Path("/search/findPostsByTagAndStationId")
	public ContentResponse<List<PostView>> findPostsByTagAndStationId(@QueryParam("tagName") String tagName, @QueryParam("stationId") Integer stationId, @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

		Pageable pageable = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));

		List<Post> posts = termRepository.findPostsByTagAndStationId(tagName, stationId, pageable);

		List<PostView> postViews = postConverter.convertToViews(posts);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}
}
