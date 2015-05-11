package com.wordrails.api;

import com.wordrails.business.Person;
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
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.persistence.WordpressRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    PersonRepository personRepository;
    private @Autowired
    StationRepository stationRepository;
    private @Autowired
    TaxonomyRepository taxonomyRepository;
    private @Autowired
    WordpressRepository wordpressRepository;

    @POST
    @Path("/createPosts")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPosts(List<WordpressPost> wpPosts) throws ServletException, IOException, URISyntaxException {
        String token = request.getHeader("token");
        Wordpress wp = wordpressRepository.findByToken(token);
        if(wp == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            Map<Integer, Term> dbTerms = new HashMap<>();
            Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
            List<Term> ts = termRepository.findByTaxonomy(tagTaxonomy);
            for (Term term : ts) {
                dbTerms.put(term.wordpressId, term);
            }

            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
            List<Term> cs = termRepository.findByTaxonomy(categoryTaxonomy);
            for (Term term : cs) {
                dbTerms.put(term.wordpressId, term);
            }

            Person author = personRepository.findByWordpress(wp);
            Station station = stationRepository.findByWordpress(wp);
            
            List<Post> posts = new ArrayList<>();
            for (WordpressPost wpPost : wpPosts) {
                Post post = getPost(wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                post.station = station;
                post.author = author;

                posts.add(post);
            }

            postRepository.save(posts);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    private Post getPost(WordpressPost wpPost, Map<Integer, Term> dbTerms, Taxonomy tagTaxonomy, Taxonomy categoryTaxonomy) {
        Post post = new Post();
        post.body = wpPost.body;
        post.state = wpPost.status;
        post.date = wpPost.date;
        post.title = wpPost.title;
        post.wordpressId = wpPost.id;
        
        if(wpPost.terms != null && (wpPost.terms.tags != null || wpPost.terms.categories != null)) {
            Set<WordpressTerm> terms = new HashSet<>(); 
            terms.addAll(wpPost.terms.tags);
            terms.addAll(wpPost.terms.categories);
            
            post.terms = getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
        }
        

        return post;
    }
    
    private Set<Term> getTerms(Collection<WordpressTerm> wsTerms, Map<Integer, Term> dbTerms, Taxonomy taxTag, Taxonomy taxCat) {
        Set<Term> terms = new HashSet();
        for (WordpressTerm term : wsTerms) {
            Term parent = null;
            Term t = dbTerms.get(term.id);
            if (t == null) {
                if (term.parent != null && term.parent > 0) {
                    parent = dbTerms.get(term.parent);
                    if(parent == null) {
                        //TODO find parent somewhere
                    }
                }
                t = new Term();
                t.wordpressId = term.id;
                t.name = term.name;
                if(term.isTag())
                    t.taxonomy = taxTag;
                else
                    t.taxonomy = taxCat;
                t.slug = term.slug;
                t.parent = parent;
        
                termRepository.save(t);
            }

            terms.add(t);
        }
        
        return terms;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(WordpressConfig config) throws ServletException, IOException {
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
                }

                wordpress.domain = config.domain;
                wordpress.username = config.user;
                wordpress.password = config.password;
                wordpress.station = station;

                station.wordpress = wordpress;
                wordpressRepository.save(wordpress);
                
                if (newWordpress) {
                    //TODO manage taxonomy to enable this
                    WordpressApi api = ServiceGenerator.createService(WordpressApi.class, config.domain, config.user, config.password);
                    importWordpress(station, station.defaultPerspectiveId, api);
                    
                    Person person = new Person();
                    person.username = "wordpress";
                    person.email = "wordpress@xarx.co";
                    
                    personRepository.save(person);
                    
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    public void importWordpress(Station station, Integer defaultPerspectiveId, WordpressApi api) {
        List<WordpressTerm> terms = api.getTags();
        terms.addAll(api.getCategories());

        //TODO enviar terms
        Map<Integer, Term> dbTerms = new HashMap<>();
        Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByStation(station);
        List<Term> ts = termRepository.findByTaxonomy(tagTaxonomy);
        for (Term term : ts) {
            dbTerms.put(term.wordpressId, term);
        }

        Taxonomy categoryTaxonomy = taxonomyRepository.findByStation(station);
        List<Term> cs = termRepository.findByTaxonomy(categoryTaxonomy);
        for (Term term : cs) {
            dbTerms.put(term.wordpressId, term);
        }
        
        Set<Term> newTerms = getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
        termRepository.save(newTerms);
    }

//    private Map<Integer, Term> saveTerms(List<WordpressTerm> terms, Taxonomy tax) {
//        Map<Integer, Term> ts = new HashMap<>();
//        for (WordpressTerm term : terms) {
//            Term t = new Term();
//            t.wordpressId = term.id;
//            t.name = term.name;
//            t.taxonomy = tax;
//            t.slug = term.slug;
//
//            ts.put(t.id, t);
//            try {
//                termRepository.save(t);
//            } catch (DataIntegrityViolationException e) {
//                //ja existe
//            }
//        }
//
//        return ts;
//    }

}
