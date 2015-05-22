package com.wordrails.api;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.wordrails.WordrailsService;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.business.Taxonomy;
import com.wordrails.business.Term;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressConfig;
import com.wordrails.business.WordpressPost;
import com.wordrails.business.WordpressTerm;
import com.wordrails.business.WordpressTerms;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.exception.ConstraintViolationException;
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
    HttpServletRequest request;

    private @Autowired
    WordrailsService wordrailsService;

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

    private HashBasedTable<String, Integer, Term> getTermsByTaxonomy(Taxonomy taxonomy) {
        HashBasedTable<String, Integer, Term> dbTerms = HashBasedTable.create();
        List<Term> ts = termRepository.findByTaxonomy(taxonomy);
        for (Term term : ts) {
            if(term.wordpressSlug == null) term.wordpressSlug = "";
            if(term.wordpressId == null) term.wordpressId = 0;
            dbTerms.put(term.wordpressSlug, term.wordpressId, term);
        }

        return dbTerms;
    }

    private Wordpress getWordpressByToken() throws UnauthorizedException {
        String token = request.getHeader("token");
        Wordpress wp = wordpressRepository.findByToken(token);
        if (wp == null) {
            throw new UnauthorizedException("Token invalido");
        }

        return wp;
    }

    @PUT
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(WordpressPost wpPost) throws ServletException, IOException, URISyntaxException {
        Wordpress wp;
        try {
            wp = getWordpressByToken();
        } catch (UnauthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
            Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
            HashBasedTable<String, Integer, Term> dbTerms = getTermsByTaxonomy(tagTaxonomy);
            dbTerms.putAll(getTermsByTaxonomy(categoryTaxonomy));

            Post post;
            Set<String> slugs = postRepository.findSlugs();
            if (request.getMethod().equals("PUT")) {
                post = postRepository.getOne(wpPost.id);

                if (post == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Post of id " + wpPost.id + " does not exist").build();
                }

                post = getPost(post, wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
            } else {
                if (postRepository.findByWordpressId(wpPost.id) != null) {
                    return Response.status(Response.Status.PRECONDITION_FAILED).type("text/plain").entity("Post already exists").build();
                }

                Person author = personRepository.findByWordpressId(1); //temporary
                Station station = stationRepository.findByWordpressId(wp.id);

                post = getPost(new Post(), wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                post.station = station;
                post.author = author;
            }

            if (!slugs.add(post.slug)) { //if slug already exists in db
                String hash = WordrailsUtil.generateRandomString(5, "!Aau");
                post.slug = post.slug + "-" + hash;
            }

            if (post != null) {
                postRepository.save(post);
                wordrailsService.processWordpressPost(post);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(ExceptionUtils.getStackTrace(e)).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @POST
    @Path("/posts")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response posts(List<WordpressPost> wpPosts) throws ServletException, IOException, URISyntaxException {
        Wordpress wp;
        try {
            wp = getWordpressByToken();
        } catch (UnauthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Post post = null;
        WordpressPost wpp = null;
        try {
            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
            Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
            HashBasedTable<String, Integer, Term> dbTerms = getTermsByTaxonomy(tagTaxonomy);
            dbTerms.putAll(getTermsByTaxonomy(categoryTaxonomy));

            Set<Post> posts = new HashSet<>();
            Set<String> slugs = postRepository.findSlugs();
            Set<Integer> wordpressIds = postRepository.findWordpressIds();
            for (WordpressPost wpPost : wpPosts) {
                wpp = wpPost;
                if (request.getMethod().equals("PUT")) {
                    post = postRepository.getOne(wpPost.id);

                    if (post == null) {
                        return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Post of id " + wpPost.id + " does not exist").build();
                    }

                    post = getPost(post, wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                } else {
                    if (!wordpressIds.add(wpPost.id)) { //if wordpressId already exists in db
                        continue;
                    }

                    Person author = personRepository.findByWordpressId(1); //temporary
                    Station station = stationRepository.findByWordpressId(wp.id);

                    post = getPost(new Post(), wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                    post.station = station;
                    post.author = author;
                }

                if (!slugs.add(post.slug)) { //if slug already exists in db
                    String hash = WordrailsUtil.generateRandomString(5, "!Aau");
                    post.slug = post.slug + "-" + hash;
                }

                posts.add(post);
            }

            postRepository.save(posts);
            wordrailsService.processWordpressPost(posts);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "";
            if (wpp != null) {
                msg = "Post id=" + wpp.id + " ";
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getClass().getSimpleName() + ": " + msg + ExceptionUtils.getStackTrace(e)).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    private Post getPost(Post post, WordpressPost wpPost, HashBasedTable<String, Integer, Term> dbTerms, Taxonomy tagTaxonomy, Taxonomy categoryTaxonomy)
        throws ConstraintViolationException, DataIntegrityViolationException, Exception {
        post.body = wpPost.body;
        switch (wpPost.status) {
            case "publish":
                post.state = Post.STATE_PUBLISHED;
                break;
            case "draft":
                post.state = Post.STATE_DRAFT;
                break;
            case "future":
                post.state = Post.STATE_SCHEDULED;
                break;
        }
        post.date = wpPost.date;
        post.slug = wpPost.slug;
        post.originalSlug = wpPost.slug;
        post.title = wpPost.title;
        post.wordpressId = wpPost.id;
        post.lastModificationDate = wpPost.modified;

        if (wpPost.terms != null && (wpPost.terms.tags != null || wpPost.terms.categories != null)) {
            Map<Integer, WordpressTerm> terms = new HashMap<>();
            if (wpPost.terms.tags != null) {
                for (WordpressTerm tag : wpPost.terms.tags) {
                    terms.put(tag.id, tag);
                }
            }
            if (wpPost.terms.categories != null) {
                for (WordpressTerm category : wpPost.terms.categories) {
                    terms.put(category.id, category);
                }
            }

            post.terms = getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
        }

        return post;
    }
    
    private Term selectTerm(HashBasedTable<String, Integer, Term> terms, Integer wordpressId, String slug, boolean isTag, Integer taxTagId) throws SecurityException {
        Term term = null;
        
        if(terms.containsColumn(wordpressId)) { //does it exist a term with this wordpressId?
            if(terms.contains(slug,wordpressId)) { //does this term have the same slug?
                term = terms.get(slug, wordpressId);
            } else if(terms.contains("",wordpressId)) { //is the slug of this term null?
                term = terms.get("", wordpressId);
                term.wordpressSlug = slug;
                
                termRepository.save(term);
            } else {//another wordpressId is using this slug. Data is fucked up somewhere. should never happen
                throw new SecurityException("Data stored in database is different from the terms sent in the request");
            }
        } else if(terms.containsRow(slug)) { //does it exist a term with this slug?
            if(terms.contains(slug,0)) { //is the wordpressId of this term null?
                term = terms.get(slug, 0);
                term.wordpressId = wordpressId;
                
                termRepository.save(term);
            } else {
                //in this case, there is another term using this slug but the wordpressId is neither null or the same. so the data is inconsistent if 
                //they belong to the same taxonomy. if they do, throw a exception
                
                Integer taxId = Lists.newArrayList(terms.rowMap().get(slug).values()).get(0).taxonomy.id;
                if((isTag && taxId == taxTagId) || (!isTag && taxId != taxTagId)) { //if they are part of same taxonomy, it should not have the same slug. otherwise no prob
                    //another wordpressId is using this slug. Data is fucked up somewhere. should never happen
                    throw new SecurityException("Data stored in database is different from the terms sent in the request");
                }
            }
        } else {
            return null;
        }
        
        return term;
    }
    
    private Term newTerm(WordpressTerm term, Taxonomy tax, Map<Integer, WordpressTerm> wpTerms, HashBasedTable<String, Integer, Term> dbTerms) {
        Term t = new Term();
        t.wordpressId = term.id;
        t.name = term.name;
        t.taxonomy = tax;
        t.wordpressSlug = term.slug;
        
        if(term.parent != null && term.parent > 0) {
            Term termParent = null;
            if(dbTerms.containsColumn(term.parent)) {
                termParent = Lists.newArrayList(dbTerms.columnMap().get(term.parent).values()).get(0);
            } else if(wpTerms.containsKey(term.parent)) {
                termParent = newTerm(wpTerms.get(term.parent), tax, wpTerms, dbTerms); //it's recursive bitch
                termRepository.save(termParent);
            }
            t.parent = termParent;
        }
        
        termRepository.save(t);
                    
        return t;
    }

    private Set<Term> getTerms(Map<Integer, WordpressTerm> wpTerms, HashBasedTable<String, Integer, Term> dbTerms, Taxonomy taxTag, Taxonomy taxCat)
        throws ConstraintViolationException, DataIntegrityViolationException, Exception {
        Set<Term> terms = new HashSet();
        for (WordpressTerm term : wpTerms.values()) {
            Term t = selectTerm(dbTerms, term.id, term.slug, term.isTag(), taxTag.id);
            if(t == null) { //does not exist. create new
                t = new Term();
                if (term.parent != null && term.parent > 0) {
                    if(dbTerms.containsColumn(term.parent)) { //if parent is in db, get from db map
                        t.parent = Lists.newArrayList(dbTerms.columnMap().get(term.parent).values()).get(0);
                    } else if(wpTerms.containsKey(term.parent)) { //if not in db, makes new and save it to db
                        t.parent = newTerm(wpTerms.get(term.parent), taxCat, wpTerms, dbTerms);
                    }
                }
                t.wordpressId = term.id;
                t.name = term.name;
                if (term.isTag()) {
                    t.taxonomy = taxTag;
                } else {
                    t.taxonomy = taxCat;
                }
                t.wordpressSlug = term.slug;                
                
                try {
                    termRepository.save(t);
                    dbTerms.put(t.wordpressSlug, t.wordpressId, t);
                } catch (ConstraintViolationException | DataIntegrityViolationException e) {
                    t = termRepository.findByWordpressSlug(term.slug);
                    if(t != null) {
                        t.wordpressId = term.id;
                        try {
                            termRepository.save(t);
                        } catch (ConstraintViolationException | DataIntegrityViolationException e2) {
                            throw new Exception("Term ID=" + term.id + " " + ExceptionUtils.getStackTrace(e), e2);
                        }
                    }
                }
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

                if (newWordpress) {
                    //TODO manage taxonomy to enable this                    
                    Map<Integer, WordpressTerm> terms = new HashMap<>();

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

                    try {
                        personRepository.save(person);
                        stationRolesRepository.save(stRole);
                    } catch (ConstraintViolationException | DataIntegrityViolationException e) {
                        //already exists
                    }

                }
            }
        } catch (Exception e) {
            Logger.getLogger(WordpressResource.class.getName()).log(Level.SEVERE, null, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
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
        HashBasedTable<String, Integer, Term> dbTags = getTermsByTaxonomy(tagTaxonomy);
        HashBasedTable<String, Integer, Term> dbCategories = getTermsByTaxonomy(categoryTaxonomy);

        for (WordpressTerm tag : tags) {
            Term t = dbTags.get(tag.slug, tag.trixId);
            t.wordpressId = tag.id;
            t.wordpressSlug = tag.slug;

            try {
                termRepository.save(t);
            } catch (ConstraintViolationException | DataIntegrityViolationException e) {
                //should never happen
            }
        }

        for (WordpressTerm cat : categories) {
            Term t = dbCategories.get(cat.slug, cat.trixId);
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

    private void saveTerms(Map<Integer, WordpressTerm> terms, Station station) throws Exception {
        Taxonomy categoryTaxonomy = taxonomyRepository.findByStation(station);
        Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByStation(station);
        HashBasedTable<String, Integer, Term> dbTerms = getTermsByTaxonomy(tagTaxonomy);
        dbTerms.putAll(getTermsByTaxonomy(categoryTaxonomy));
        //TODO enviar terms

        Set<Term> newTerms;
        try {
            newTerms = getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
            termRepository.save(newTerms);
        } catch (DataIntegrityViolationException ex) {
            Logger.getLogger(WordpressResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WordpressResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
