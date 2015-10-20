package com.wordrails.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.TermPerspective;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.Encoder;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.FullTextQuery;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.business.BadRequestException;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.PostRepository;
import com.wordrails.util.WordrailsUtil;

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