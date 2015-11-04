package com.wordrails.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.wordrails.auth.TrixAuthenticationProvider;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.FullTextQuery;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.PostRepository;

@Path("/images")
@Consumes(MediaType.WILDCARD)
@Component
public class ImagesResource {
	private @Context HttpServletRequest request;
	private @Context UriInfo uriInfo;
	private @Context HttpServletResponse response;

	private @Autowired WordrailsService wordrailsService;
	private @Autowired PostRepository postRepository;
	private @Autowired PostConverter postConverter;

	private @PersistenceContext EntityManager manager;
	
	private @Autowired
	TrixAuthenticationProvider authProvider;
//
//	TermPerspective tp = termPerspectiveRepository.findPerspectiveAndTerm(perspectiveId, termId);
//
//	String hash = ""; // termRepository.findValidHash(perspectiveId, termId);
//
//	if(tp != null && tp.defaultImageHash != null)
//	hash = tp.defaultImageHash;
//	else
//			postRepository.findByFeaturedImageByTermId(termId);

}