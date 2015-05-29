package com.wordrails.api;

import com.google.common.collect.HashBasedTable;
import com.wordrails.business.Person;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.business.Taxonomy;
import com.wordrails.business.Term;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressConfig;
import com.wordrails.business.WordpressService;
import com.wordrails.business.WordpressTerm;
import com.wordrails.business.WordpressTerms;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.persistence.WordpressRepository;
import java.io.IOException;
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
import javax.ws.rs.GET;
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

    @Context
    private HttpServletRequest request;
    @Autowired
    private WordpressService wordpressService;

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
    @Path("/sync")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startSync(WordpressConfig config) {
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

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(WordpressConfig config) {
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
        HashBasedTable<String, Integer, Term> dbTags = wordpressService.getTermsByTaxonomy(tagTaxonomy);
        HashBasedTable<String, Integer, Term> dbCategories = wordpressService.getTermsByTaxonomy(categoryTaxonomy);

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
        HashBasedTable<String, Integer, Term> dbTerms = wordpressService.getTermsByTaxonomy(tagTaxonomy);
        dbTerms.putAll(wordpressService.getTermsByTaxonomy(categoryTaxonomy));
        //TODO enviar terms

        Set<Term> newTerms;
        try {
            newTerms = wordpressService.getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
            termRepository.save(newTerms);
        } catch (DataIntegrityViolationException ex) {
            Logger.getLogger(WordpressResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WordpressResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
