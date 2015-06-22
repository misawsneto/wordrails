package com.wordrails.api;

import com.google.common.collect.HashBasedTable;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import com.wordrails.util.WordrailsUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author arthur
 */
@Path("/wp")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WordpressResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private WordpressService wordpressService;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private WordpressRepository wordpressRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;

	private Wordpress getWordpressByToken() throws UnauthorizedException {
		String token = request.getHeader("token");
		Wordpress wp = wordpressRepository.findByToken(token);
		if (wp == null) {
			throw new UnauthorizedException("Token invalido");
		}

		return wp;
	}

	@GET
	@Path("/syncposts")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response syncPosts() {
		Wordpress wp;
		try {
			wp = getWordpressByToken();
		} catch (UnauthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
		wordpressService.sync(wp, api);

		return Response.status(Response.Status.OK).build();
	}

	@PUT
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(WordpressPost wpPost) {
		Wordpress wp;
		try {
			wp = getWordpressByToken();
		} catch (UnauthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		try {
			Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
			Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
			HashBasedTable<String, Integer, Term> dbTerms = wordpressService.getTermsByTaxonomy(tagTaxonomy);
			dbTerms.putAll(wordpressService.getTermsByTaxonomy(categoryTaxonomy));

			Post post;
			Set<String> slugs = postRepository.findSlugs();
			if (request.getMethod().equals("PUT")) {
				post = postRepository.getOne(wpPost.id);

				if (post == null) {
					return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Post of id " + wpPost.id + " does not exist").build();
				}

				post = wordpressService.getPost(post, wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
			} else {
				if (postRepository.findByWordpressId(wpPost.id) != null) {
					return Response.status(Response.Status.PRECONDITION_FAILED).type("text/plain").entity("Post already exists").build();
				}

				Person author = personRepository.findByWordpressId(1); //temporary
				Station station = stationRepository.findByWordpressId(wp.id);

				post = wordpressService.getPost(new Post(), wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
				post.station = station;
				post.author = author;
			}

			if (!slugs.add(post.slug)) { //if slug already exists in db
				String hash = WordrailsUtil.generateRandomString(5, "!Aau");
				post.slug = post.slug + "-" + hash;
			}

			if (post != null) {
				postRepository.save(post);
				wordpressService.processWordpressPost(post);
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(ExceptionUtils.getStackTrace(e)).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(WordpressConfig config) {
		if (config == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		int termsSaved = 0;
		Map<Integer, WordpressTerm> terms = new HashMap<>();
		try {
			Station station = stationRepository.findByWordpressToken(config.token);
			if (station == null || station.wordpress == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Token is not created for this station").build();
			}

			Wordpress wordpress = station.wordpress;
			if (wordpress.domain == null || wordpress.domain.isEmpty()) { //new login
				//TODO manage taxonomy to enable this

				if (config.terms != null) {
					if (config.terms.tags != null) {
						for (WordpressTerm tag : config.terms.tags) {
							terms.put(tag.id, tag);
						}
					}
					if (config.terms.categories != null) {
						for (WordpressTerm category : config.terms.categories) {
							terms.put(category.id, category);
						}
					}
				}

				if (terms.isEmpty()) {
					WordpressApi api = ServiceGenerator.createService(WordpressApi.class, config.domain, config.user, config.password);
					Set<WordpressTerm> tags = api.getTags();
					Set<WordpressTerm> cats = api.getCategories();
					if (tags != null) {
						for (WordpressTerm tag : tags) {
							terms.put(tag.id, tag);
						}
					}
					if (cats != null) {
						for (WordpressTerm category : cats) {
							terms.put(category.id, category);
						}
					}
				}

				wordpress.domain = config.domain;
				wordpress.username = config.user;
				wordpress.password = config.password;
				wordpress.station = station;

				station.wordpress = wordpress;
				wordpressRepository.save(wordpress);

				termsSaved = saveTerms(terms, station);

				//temporary
				Person person = new Person();
				person.username = "wordpress";
				person.email = "wordpress@xarx.co";
				person.wordpressId = 1;
				person.personsStationPermissions = new HashSet<>();
				StationRole stRole = new StationRole();
				stRole.admin = true;
				stRole.editor = true;
				stRole.writer = true;
				stRole.person = person;
				stRole.station = station;
				stRole.wordpress = wordpress;
				person.personsStationPermissions.add(stRole);

				try {
					personRepository.save(person);
					stationRolesRepository.save(stRole);
				} catch (ConstraintViolationException | DataIntegrityViolationException e) {
					//already exists
				}

			}
		} catch (Exception e) {
			Logger.getLogger(WordpressResource.class.getName()).log(Level.ERROR, null, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
		}

		return Response.status(Response.Status.OK).type("text/plain").entity("Terms received: " + terms.size() + ", Terms saved: " + termsSaved).build();
	}

	@POST
	@Path("/term")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response term(WordpressTerm term) throws ServletException, IOException {
		Wordpress wp;
		try {
			wp = getWordpressByToken();
		} catch (UnauthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		Taxonomy taxonomy = null;

		if (term.isTag()) {
			taxonomy = taxonomyRepository.findTypeTByWordpress(wp);
		} else {
			taxonomy = taxonomyRepository.findByWordpress(wp);
		}

		Term t = new Term();
		Term parent = termRepository.findByWordpressIdAndTaxonomy(term.parent, taxonomy);
		t.name = term.name;
		t.wordpressId = term.id;
		t.wordpressSlug = term.slug;
		t.parent = parent;
		t.taxonomy = taxonomy;

		try {
			termRepository.save(t);
		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
			//should never happen
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Term is already created").build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@PUT
	@Path("/terms")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response terms(WordpressTerms terms) throws ServletException, IOException {
		Wordpress wp;
		try {
			wp = getWordpressByToken();
		} catch (UnauthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		Set<WordpressTerm> tags = terms.tags;
		Set<WordpressTerm> categories = terms.categories;

		Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
		Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
		HashBasedTable<String, Integer, Term> dbTags = wordpressService.getTermsByTaxonomy(tagTaxonomy);
		HashBasedTable<String, Integer, Term> dbCategories = wordpressService.getTermsByTaxonomy(categoryTaxonomy);

		for (WordpressTerm tag : tags) {
			Term t = dbTags.get(tag.slug, tag.id);
			t.wordpressId = tag.id;
			t.wordpressSlug = tag.slug;

			try {
				termRepository.save(t);
			} catch (ConstraintViolationException | DataIntegrityViolationException e) {
				//should never happen
			}
		}

		for (WordpressTerm cat : categories) {
			Term t = dbCategories.get(cat.slug, cat.id);
			t.wordpressId = cat.id;
			t.wordpressSlug = cat.slug;

			try {
				termRepository.save(t);
			} catch (ConstraintViolationException | DataIntegrityViolationException e) {
				//should never happen
			}
		}

		return Response.status(Response.Status.OK).build();
	}

	private Integer saveTerms(Map<Integer, WordpressTerm> terms, Station station) throws Exception {
		Taxonomy categoryTaxonomy = taxonomyRepository.findByStation(station);
		Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByStation(station);
		HashBasedTable<String, Integer, Term> dbTerms = HashBasedTable.create();
		dbTerms.putAll(wordpressService.getTermsByTaxonomy(tagTaxonomy));
		dbTerms.putAll(wordpressService.getTermsByTaxonomy(categoryTaxonomy));
		//TODO enviar terms

		Set<Term> newTerms = wordpressService.findAndSaveTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);

		return newTerms.size();
	}
}
