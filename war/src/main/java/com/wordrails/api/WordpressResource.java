package com.wordrails.api;

import com.wordrails.business.Post;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Station;
import com.wordrails.business.Taxonomy;
import com.wordrails.business.Term;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressConfig;
import com.wordrails.business.WordpressPost;
import com.wordrails.business.WordpressTerm;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.persistence.WordpressRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author arthur
 */
@Path("/wp")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WordpressResource {

    private @Context
    UriInfo uriInfo;
    private @Context
    HttpServletRequest request;

    private @Autowired
    TermRepository termRepository;
    private @Autowired
    PostRepository postRepository;
    private @Autowired
    StationRepository stationRepository;
    private @Autowired
    TaxonomyRepository taxonomyRepository;
    private @Autowired
    WordpressRepository wordpressRepository;
    private @Autowired
    StationPerspectiveRepository stationPerspectiveRepository;

    @POST
    @Path("/createPosts")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPosts(List<WordpressPost> wpPosts) throws ServletException, IOException {
        String path = request.getServletPath() + uriInfo.getPath();

        try {
            Map<Integer, Term> termsTag = new HashMap<>();
            Taxonomy tagTaxonomy = taxonomyRepository.findByWordpressDomain(path);
            List<Term> ts = termRepository.findByTaxonomy(tagTaxonomy);
            for (Term term : ts) {
                termsTag.put(term.id, term);
            }

            Map<Integer, Term> termsCategories = new HashMap<>();
            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpressDomain(path);
            List<Term> cs = termRepository.findByTaxonomy(categoryTaxonomy);
            for (Term term : cs) {
                termsCategories.put(term.id, term);
            }

            List<Post> posts = new ArrayList<>();
            for (WordpressPost wpPost : wpPosts) {
                Post post = getPost(wpPost, termsTag, termsCategories, tagTaxonomy, categoryTaxonomy);

                posts.add(post);
            }

            postRepository.save(posts);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    private Post getPost(WordpressPost wpPost, Map<Integer, Term> termsTag, Map<Integer, Term> termsCategories, Taxonomy tagTaxonomy, Taxonomy categoryTaxonomy) {
        Post post = new Post();
        post.body = wpPost.body;
        post.state = wpPost.status;
        post.date = wpPost.date;
        post.title = wpPost.title;
        post.wordpressId = wpPost.id;
        post.terms = new HashSet();
        for (WordpressTerm t : wpPost.tags) {
            Term term = termsTag.get(t.id);
            if (term == null) {
                term = getTerm(t, tagTaxonomy, termsTag);
                termRepository.save(term);
            }

            post.terms.add(term);
        }
        for (WordpressTerm t : wpPost.categories) {
            Term term = termsCategories.get(t.id);
            if (term == null) {
                term = getTerm(t, categoryTaxonomy, termsCategories);
                termRepository.save(term);
            }

            post.terms.add(term);
        }

        return post;
    }

    private Term getTerm(WordpressTerm term, Taxonomy taxonomy, Map<Integer, Term> terms) {
        Term t = new Term();

        t.id = term.id;
        t.name = term.name;
        t.taxonomy = taxonomy;
        if (term.parent != null && term.parent > 0) {
            t.parent = terms.get(term.parent);
        }

        return t;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response postWordpressConfig(WordpressConfig config) throws ServletException, IOException {

        boolean newWordpress = false;
        try {
            List<Station> stations = stationRepository.findAll();
            Station station = null;
            Wordpress wordpress = null;
            if (!stations.isEmpty()) {
                for (Station st : stations) {
                    if (st.wordpress != null) {
                        if (st.wordpress.token.equals(config.token)) {
                            station = st;
                            wordpress = st.wordpress;
                            newWordpress = wordpress.domain == null;
                            break;
                        }
                    }
                }

                if (wordpress == null) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Token is not created for this station").build();
                } else if (newWordpress) {
                    //TODO manage taxonomy to enable this
                    WordpressApi api = ServiceGenerator.createService(WordpressApi.class, config.domain, config.user, config.password);
                    importWordpress(station, station.defaultPerspectiveId, api);
                }

                wordpress.domain = config.domain;
                wordpress.username = config.user;
                wordpress.password = config.password;
                wordpress.station = station;

                station.wordpress = wordpress;
                wordpressRepository.save(wordpress);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    public void importWordpress(Station station, Integer defaultPerspectiveId, WordpressApi api) {
        List<WordpressTerm> tags = api.getTags();
        List<WordpressTerm> categories = api.getCategories();

        //TODO enviar terms
//        Map<Integer, Term> termsTag = new HashMap<>();
//        Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByStation(station);
//        List<Term> ts = termRepository.findByTaxonomy(tagTaxonomy);
//        for (Term term : ts) {
//            termsTag.put(term.id, term);
//        }
//
//        Map<Integer, Term> termsCategories = new HashMap<>();
//        Taxonomy categoryTaxonomy = taxonomyRepository.findByStation(station);
//        List<Term> cs = termRepository.findByTaxonomy(categoryTaxonomy);
//        for (Term term : cs) {
//            termsCategories.put(term.id, term);
//        }
        Taxonomy tax = taxonomyRepository.findAuthorTaxonomyByStationId(station, "T");
        saveTerms(tags, tax);

        tax = stationPerspectiveRepository.findOne(defaultPerspectiveId).taxonomy;
        saveTerms(categories, tax);
    }

    private Map<Integer, Term> saveTerms(List<WordpressTerm> terms, Taxonomy tax) {
        Map<Integer, Term> ts = new HashMap<>();
        for (WordpressTerm term : terms) {
            Term t = new Term();
            t.wordpressId = term.id;
            t.name = term.name;
            t.taxonomy = tax;

            ts.put(t.id, t);
            try {
                termRepository.save(t);
            } catch (DataIntegrityViolationException e) {
                //ja existe
            }
        }

        return ts;
    }

}
