package com.wordrails.api;

import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
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
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.persistence.WordpressRepository;
import com.wordrails.util.WordrailsUtil;
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
import org.hibernate.exception.ConstraintViolationException;
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
    private @Autowired
    StationRolesRepository stationRolesRepository;

    private Map<Integer, Term> getTerms(Taxonomy taxonomy) {
        Map<Integer, Term> dbTerms = new HashMap<>();
        List<Term> ts = termRepository.findByTaxonomy(taxonomy);
        for (Term term : ts) {
            dbTerms.put(term.wordpressId, term);
        }

        return dbTerms;
    }

    @POST
    @Path("/createPost")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPost(WordpressPost wpPost) throws ServletException, IOException, URISyntaxException {
        String token = request.getHeader("token");
        Wordpress wp = wordpressRepository.findByToken(token);
        if (wp == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
            Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
            Map<Integer, Term> dbTerms = getTerms(tagTaxonomy);
            dbTerms.putAll(getTerms(categoryTaxonomy));

            Person author = personRepository.findByWordpressId(1); //temporary
            Station station = stationRepository.findByWordpress(wp);

            Post post = getPost(wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
            post.station = station;
            post.author = author;
            
            int countSlug = postRepository.countSlugPost(post.slug);
            if(countSlug > 0) {
                String hash = WordrailsUtil.generateRandomString(5, "!Aau");
				post.slug = post.slug + "-" + hash;
            }
            
            postRepository.save(post);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/createPosts")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPosts(List<WordpressPost> wpPosts) throws ServletException, IOException, URISyntaxException {
        String token = request.getHeader("token");
        Wordpress wp = wordpressRepository.findByToken(token);
        if (wp == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
            Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
            Map<Integer, Term> dbTerms = getTerms(tagTaxonomy);
            dbTerms.putAll(getTerms(categoryTaxonomy));

            Person author = personRepository.findByWordpressId(1); //temporary
            Station station = stationRepository.findByWordpress(wp);

            List<Post> posts = new ArrayList<>();
            for (WordpressPost wpPost : wpPosts) {
                Post post = getPost(wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                post.station = station;
                post.author = author;
                
                int countSlug = postRepository.countSlugPost(post.slug);
                if(countSlug > 0) {
                    String hash = WordrailsUtil.generateRandomString(5, "!Aau");
                    post.slug = post.slug + "-" + hash;
                }

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
        switch(wpPost.status) {
            case "publish":
                post.state = Post.STATE_PUBLISHED; break;
            case "draft":
                post.state = Post.STATE_DRAFT; break;
            case "future":
                post.state = Post.STATE_SCHEDULED; break;                
        }
        post.date = wpPost.date;
        post.slug = wpPost.slug;
        post.originalSlug = wpPost.slug;
        post.title = wpPost.title;
        post.wordpressId = wpPost.id;

        if (wpPost.terms != null && (wpPost.terms.tags != null || wpPost.terms.categories != null)) {
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
                    if (parent == null) {
                        //TODO find parent somewhere
                    }
                }
                t = new Term();
                t.wordpressId = term.id;
                t.name = term.name;
                if (term.isTag()) {
                    t.taxonomy = taxTag;
                } else {
                    t.taxonomy = taxCat;
                }
                t.wordpressSlug = term.slug;
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
                    Set<WordpressTerm> terms = new HashSet<>();
                    
                    if(config.terms != null) {
                        terms.addAll(config.terms.tags);
                        terms.addAll(config.terms.categories);
                    }
                    
                    if(terms.isEmpty()) {                    
                        WordpressApi api = ServiceGenerator.createService(WordpressApi.class, config.domain, config.user, config.password);
                        terms.addAll(api.getTags());
                        terms.addAll(api.getCategories());
                    }
                    
                    saveTerms(terms, station);

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

                    personRepository.save(person);
                    stationRolesRepository.save(stRole);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    private void saveTerms(Set<WordpressTerm> terms, Station station) {
        Taxonomy categoryTaxonomy = taxonomyRepository.findByStation(station);
        Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByStation(station);
        Map<Integer, Term> dbTerms = getTerms(tagTaxonomy);
        dbTerms.putAll(getTerms(categoryTaxonomy));
        //TODO enviar terms
            
        Set<Term> newTerms = getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
        for (Term newTerm : newTerms) {
            try {
                termRepository.save(newTerm);
            } catch (ConstraintViolationException e1) {
                //already exists
            }
        }
    }
}
