package com.wordrails.web.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.api.ContentResponse;
import com.wordrails.api.PostView;
import com.wordrails.api.TermView;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.domain.*;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.*;
import com.wordrails.services.AmazonCloudService;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.converter.TermConverter;

@Path("/terms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class TermsResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired PersonRepository personRepository;

	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired TermRepository termRepository;
	private @Autowired PostConverter postConverter;
	private @Autowired TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired
	TermConverter termConverter;
	private @Autowired @Qualifier("simpleMapper")
	ObjectMapper simpleMapper;
	private @Autowired @Qualifier("objectMapper")
	ObjectMapper mapper;

	private @Autowired
	AmazonCloudService amazonCloudService;

	private @Autowired
	PostRepository postRepository;

	@GET
	@Path("/termTree")
	public Response getTermTree(@QueryParam("taxonomyId") Integer taxonomyId, @QueryParam("perspectiveId") Integer perspectiveId) throws JsonGenerationException, JsonMappingException, IOException {
		List<Term> allTerms;
		if(perspectiveId != null){
			allTerms = termRepository.findByPerspectiveId(perspectiveId);
		}else{
			allTerms = termRepository.findByTaxonomyId(taxonomyId);
		}

		List<Term> roots =  wordrailsService.createTermTree(allTerms);

		String json = simpleMapper.writeValueAsString(roots);
		return Response.status(Status.OK).entity(json).build();
	}

	@GET
	@Path("/{termId}/image")
	public Response getTermImage(@PathParam("termId") Integer termId, @QueryParam("perspectiveId") Integer perspectiveId, @Context HttpServletResponse response, @Context HttpServletRequest request) throws IOException {

		String subdomain = "";
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		if (network != null)
			subdomain = network.subdomain;

		TermPerspective tp = termPerspectiveRepository.findPerspectiveAndTerm(perspectiveId, termId);
		String hash = ""; // termRepository.findValidHash(perspectiveId, termId);

		if(tp != null && tp.defaultImageHash != null)
			hash = tp.defaultImageHash;
		else {
			Pageable page = new PageRequest(0,1, Sort.Direction.DESC, "date");
			List<Post> posts = postRepository.findByFeaturedImageByTermId(termId, page);
			if(posts!=null && posts.size()>0)
				hash = posts.get(0).featuredImage.largeHash;
		}

		if (hash != null && !hash.isEmpty()) {
			response.sendRedirect(amazonCloudService.getPublicImageURL(subdomain, hash));
			return Response.ok().build();
		}

		return Response.status(Status.NO_CONTENT).build();
	}

//	@GET
//	@Path("/termWithImage")
//	public Response getTermTree(@QueryParam("perspectiveId") Integer perspectiveId) throws JsonGenerationException, JsonMappingException, IOException {
//		List<Term> allTerms = new ArrayList<Term>();
//		if(perspectiveId != null){
//			allTerms = termRepository.findByPerspectiveId(perspectiveId);
//		}
//
//		//	TermPerspective tp = termPerspectiveRepository.findPerspectiveAndTerm(perspectiveId, termId);
//		//
//		//	String hash = ""; // termRepository.findValidHash(perspectiveId, termId);
//		//
//		//	if(tp != null && tp.defaultImageHash != null)
//		//	hash = tp.defaultImageHash;
//		//	else
//		//	postRepository.findByFeaturedImageByTermId(termId);
//
//		List<TermView> termViews = new ArrayList<TermView>();
//		for(Term term: allTerms){
//			TermView tv = termConverter.convertToView(term);
//			termViews.add(tv);
//			TermPerspective tp = termPerspectiveRepository.findPerspectiveAndTerm(perspectiveId, term.id);
//			if(tp != null && tp.defaultImageHash != null)
//				tv.imageHash = tp.defaultImageHash;
//			else
//				termRepository.findValidHash(perspectiveId, termId, );
//		}
//
//		String json = simpleMapper.writeValueAsString(roots);
//		return Response.status(Status.OK).entity(json).build();
//	}

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
