package com.wordrails.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.elasticsearch.BookmarkEsRespository;
import com.wordrails.elasticsearch.PerspectiveEsRepository;
import com.wordrails.elasticsearch.PostEsRepository;
import com.wordrails.eventhandler.*;
import com.wordrails.jobs.SimpleJob;
import com.wordrails.persistence.*;
import com.wordrails.script.ImageScript;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.AsyncService;
import com.wordrails.services.CacheService;
import com.wordrails.util.WordpressParsedContent;
import com.wordrails.util.TrixUtil;
import org.jboss.resteasy.spi.HttpRequest;
import org.joda.time.DateTime;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.*;
import java.util.regex.Pattern;

@Path("/util")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UtilResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired
	NetworkEventHandler networkEventHandler;
	private @Autowired
	PersonEventHandler personEventHandler;
	private @Autowired
	TaxonomyEventHandler taxonomyEventHandler;
	private @Autowired
	StationEventHandler stationEventHandler;
	private @Autowired PersonRepository personRepository;
	private @Autowired
	StationRoleEventHandler stationRoleEventHandler;
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired PostRepository postRepository;
	private @Autowired TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;
	private @Autowired InvitationRepository invitationRepository;
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	public @Autowired FileRepository fileRepository;
	public @Autowired PerspectiveEsRepository perspectiveEsRepository;

	public @Autowired CacheService cacheService;

	private @PersistenceContext EntityManager manager;

	private @Autowired
	PostEsRepository postEsRepository;

	private @Autowired
	BookmarkEsRespository bookmarkEsRespository;

	@GET
	@Path("/updateDefaultStationPerspective")
	public Response updateDefaultStationPerspective(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Station> stations = stationRepository.findAll();
			for (Station station : stations) {
				Set	<StationPerspective> perspectives = station.stationPerspectives != null ? station.stationPerspectives : new HashSet<StationPerspective>();
				if(perspectives.size() > 0){
					station.defaultPerspectiveId = ((StationPerspective)perspectives.toArray()[0]).id;
				}
			}
			stationRepository.save(stations);
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updatePostFields")
	public Response updatePostFields(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> posts = postRepository.findAll();
			for (Post post : posts) {
				if(post.featuredImage != null && post.featuredImage.originalHash != null){
					post.imageHash = post.featuredImage.originalHash;
					post.imageSmallHash = post.featuredImage.smallHash;
					post.imageMediumHash = post.featuredImage.mediumHash;
					post.imageLargeHash = post.featuredImage.largeHash;
					post.imageLandscape= !post.featuredImage.vertical;
					post.imageCaptionText = post.featuredImage.caption;
					post.imageCreditsText = post.featuredImage.credits;
				}else{
					post.imageHash = null;
					post.imageSmallHash = null;
					post.imageMediumHash = null;
					post.imageLargeHash = null;
				}
				if(post.comments != null){
					post.commentsCount = post.comments.size();
				}
				post.readTime = Post.calculateReadTime(post.body);
			}
			postRepository.save(posts);
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateTermPerspectivesStationIds")
	public Response updateTermPerspectivesStationIds(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<TermPerspective> termPerspectives = termPerspectiveRepository.findAll();
			for (TermPerspective termPerspective : termPerspectives) {
				termPerspective.stationId = termPerspective.perspective.station.id;
			}
			termPerspectiveRepository.save(termPerspectives);

			List<StationPerspective> stationPerspectives = stationPerspectiveRepository.findAll();
			for (StationPerspective stationPerspective : stationPerspectives) {
				stationPerspective.stationId = stationPerspective.station.id;
			}
			stationPerspectiveRepository.save(stationPerspectives);
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updatePersonFields")
	public Response updatePersonFields(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Person> persons = personRepository.findAll();

			Iterator<Person> it = persons.iterator();
			while (it.hasNext()) {
				Person person =  it.next();

				if(!Pattern.matches("^[a-z0-9\\._-]{3,50}$", person.username)){
					System.err.println("Invalid username: "+ person.username + ", skipping");
					it.remove();
				}

				if(person.email == null || person.email.trim().equals("")){
					person.email = person.username + TrixUtil.generateRandomString(4, "a#")  + "@randomfix.com";
				}

				try {
					InternetAddress emailAddr = new InternetAddress(person.email);
					emailAddr.validate();
				} catch (AddressException ex) {
					System.err.println("Invalid email: "+ person.email + ", skipping");
					it.remove();
				}

				if(person.image != null && person.image.originalHash != null){
					person.imageHash = person.image.originalHash;
					person.imageSmallHash = person.image.smallHash;
					person.imageMediumHash = person.image.mediumHash;
					person.imageLargeHash = person.image.largeHash;
				}else{
					person.imageHash = null;
					person.imageSmallHash = null;
					person.imageMediumHash = null;
					person.imageLargeHash = null;
				}

				if(person.cover != null && person.cover.originalHash != null){
					person.coverMediumHash = person.cover.mediumHash;
					person.coverLargeHash = person.cover.largeHash;
					person.coverMediumHash = person.cover.mediumHash;
				}

				if(person.createdAt == null){
					person.createdAt = new DateTime(2015, 1, 17, 10, 31, 2, 0).toDate();
				}

				person.email = person.email.trim();

				try {
					personRepository.save(person);
				}catch (javax.validation.ConstraintViolationException e){
					for (ConstraintViolation v:  e.getConstraintViolations()){
						System.out.println(v.getInvalidValue());
						System.out.println(v.getPropertyPath());
					}
				}
			}

		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/recalculateSlug")
	public Response recalculateSlug(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> posts = postRepository.findAll();
			for (Post post : posts) {
				doSlug(post);
			}
		}
		return Response.status(Status.OK).build();
	}

	@POST
	@Path("/generateInvitations")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response generate(@FormParam("subdomain") String subdomain, @FormParam("stationId") Integer stationId, @FormParam("count") Integer count){

		Network network = networkRepository.findOneBySubdomain(subdomain);
		Station station = stationId != null ? stationRepository.findOne(stationId) : null;

		List<Invitation> invites = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			Invitation invitation =  new Invitation();
			invitation.network = network;
			invitation.station = station;
			invitation.active = true;
			invitation.hash = TrixUtil.generateRandomString(8, "aA#");
			invites.add(invitation);
		}

		invitationRepository.save(invites);
		return Response.status(Status.OK).build();
	}

	private void doSlug(Post post){
		String originalSlug = TrixUtil.toSlug(post.title);
		post.originalSlug = originalSlug;
		try {
			post.slug = originalSlug;
			postRepository.save(post);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			String hash = TrixUtil.generateRandomString(5, "Aa#");
			post.slug = originalSlug + "-" + hash;
			postRepository.save(post);
		}
	}

	@GET
	@Path("/updateRegIdsAndTokens")
	@Transactional
	public Response updateRegIdsAndTokens(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			manager.createNativeQuery("UPDATE PersonNetworkRegId reg set person_id = null where reg.person_id = 1").executeUpdate();
			manager.createNativeQuery("UPDATE PersonNetworkToken tok set person_id = null where tok.person_id = 1").executeUpdate();
		}
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/updateNetworkProperties")
	@Transactional
	public Response updateNetworkProperties(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Network> networks = networkRepository.findAll();
			for (Network network : networks) {
				if(network.defaultOrientationMode == null || network.defaultOrientationMode.isEmpty() || !(network.defaultOrientationMode.equals("H") || network.defaultOrientationMode.equals("V") ))
					network.defaultOrientationMode = "V";
				
				if(network.defaultReadMode == null || network.defaultReadMode.isEmpty() || !(network.defaultReadMode.equals("D") || network.defaultReadMode.equals("N") ))
					network.defaultReadMode = "D";
			}
			networkRepository.save(networks);
		}
		return Response.status(Status.OK).build();
	}

    @GET
    @Path("/updateTermPerspectives")
    @Transactional(readOnly = false)
    public Response updateTermPerspectives(@Context HttpServletRequest request){
        List<StationPerspective> sps = stationPerspectiveRepository.findAll();

        for(StationPerspective stationPerspective: sps) {

            if(stationPerspective.perspectives != null && stationPerspective.perspectives.size() > 0)
                continue;

            Taxonomy taxonomy = taxonomyRepository.findOne(stationPerspective.taxonomy.id);
            TermPerspective tp = new TermPerspective();
            tp.perspective = stationPerspective;
            tp.stationId = stationPerspective.station.id;
            tp.rows = new ArrayList<Row>();
            for (Term term: taxonomy.terms){
                Row row = new Row();
                row.term = term;
                row.type = Row.ORDINARY_ROW;
                tp.rows.add(row);
                row.perspective = tp;
            }

            stationPerspective.perspectives = new HashSet<TermPerspective>();
            stationPerspective.perspectives.add(tp);

            termPerspectiveRepository.save(tp);

            for(Row row: tp.rows){
                row.perspective = tp;
                rowRepository.save(row);
            }
        }

        return Response.status(Status.OK).build();
    }

	@GET
	@Path("/updateNetworkTaxonomies")
	@Transactional(readOnly=false)
	public Response updateNetworkTaxonomies(@Context HttpServletRequest request){
		String host = request.getHeader("Host");
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")) {
			List<Network> networks = networkRepository.findAll();
			for(Network net: networks) {
				List<Taxonomy> taxs = taxonomyRepository.findNetworkCategories(net.id);
				if(taxs != null && taxs.size() == 0){
					Taxonomy nTaxonomy = new Taxonomy();
					nTaxonomy.name = "Categoria da Rede " + net.name;
					nTaxonomy.type = Taxonomy.NETWORK_TAXONOMY;

					nTaxonomy.owningNetwork = net;

					Term nterm1 = new Term();
					nterm1.name = "Categoria 1";

					Term nterm2 = new Term();
					nterm2.name = "Categoria 2";

					nterm1.taxonomy = nTaxonomy;
					nterm2.taxonomy = nTaxonomy;

					taxonomyRepository.save(nTaxonomy);

					nTaxonomy.terms = new HashSet<Term>();
					nTaxonomy.terms.add(nterm1);
					nTaxonomy.terms.add(nterm2);
					termRepository.save(nterm1);
					termRepository.save(nterm2);

					Set<Taxonomy> nTaxonomies = new HashSet<Taxonomy>();
					nTaxonomies.add(nTaxonomy);

					taxonomyRepository.save(nTaxonomy);
					net.ownedTaxonomies = nTaxonomies;
					net.categoriesTaxonomyId = nTaxonomy.id;
					networkRepository.save(net);
				}
			}

            List<Taxonomy> taxs = manager.createQuery("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'N' and taxonomy.owningNetwork is null").getResultList();


            for(Taxonomy tax: taxs) {
                taxonomyRepository.deleteTaxonomyNetworks(tax.id);
                for (Term term : termRepository.findRoots(tax.id)) {

                    if(term.termPerspectives != null && term.termPerspectives.size() > 0){
                        termPerspectiveRepository.delete(term.termPerspectives);
                    }

                    List<Row> rows = rowRepository.findByTerm(term);
                    if(rows != null && rows.size() > 0){
                        rowRepository.delete(rows);
                    }

                    List<Term> terms = termRepository.findByParent(term);
                    if(terms != null && terms.size() > 0){
                        deleteCascade(term);
                    }
                    termRepository.deletePostsTerms(term.id);
                    if(!term.equals(term)){
                        termRepository.delete(term);
                    }

                    termRepository.delete(term);
                }
                List<StationPerspective> stationsPerspectives = stationPerspectiveRepository.findByTaxonomy(tax);
                if(stationsPerspectives != null && stationsPerspectives.size() > 0){
                    stationPerspectiveRepository.delete(stationsPerspectives);
                }
                taxonomyRepository.delete(tax);
            }

			taxs = manager.createQuery("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'N' and taxonomy.owningNetwork is not null").getResultList();

			for(Taxonomy tax: taxs){
				Network net = networkRepository.findOne(tax.owningNetwork.id);
                if(net.categoriesTaxonomyId == null) {
                    net.categoriesTaxonomyId = tax.id;
                    networkRepository.save(net);
                }
			}
        }

		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/deleteTaxonomy/{id}")
	public Response deleteTaxonomy(@Context HttpServletRequest request, @PathParam("id") Integer id){
		String host = request.getHeader("Host");
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			Taxonomy taxonomy = taxonomyRepository.findOne(id);
			Network net = networkRepository.findOne(taxonomy.owningNetwork.id);
			adminAuth(net);
			taxonomyEventHandler.handleBeforeDelete(taxonomy);
			taxonomyRepository.delete(taxonomy);
			authProvider.logout();
		}
		return Response.status(Status.OK).build();
	}

    @GET
    @Path("/updateStationTaxonomies")
    @Transactional(readOnly=false)
    public Response updateStationTaxonomies(@Context HttpServletRequest request){
        String host = request.getHeader("Host");

        if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
           List<Station> stations = stationRepository.findAll();
            for (Station station: stations){
				Set<Taxonomy> taxonomies = station.ownedTaxonomies;
				boolean hasStationTaxonomy = false;
				for (Taxonomy tax: taxonomies){
					if(tax.type.equals(Taxonomy.STATION_TAXONOMY))
					hasStationTaxonomy = true;
				}
				if(!hasStationTaxonomy){
					//Station Default Taxonomy
					Taxonomy sTaxonomy = new Taxonomy();
					sTaxonomy.name = "Station: " + station.name;
					sTaxonomy.owningStation = station;
					sTaxonomy.type = Taxonomy.STATION_TAXONOMY;
					taxonomyRepository.save(sTaxonomy);
					station.ownedTaxonomies.add(sTaxonomy);
					stationRepository.save(station);

					Term term1 = new Term();
					term1.name = "Categoria 1";

					Term term2 = new Term();
					term2.name = "Categoria 2";

					term1.taxonomy = sTaxonomy;
					term2.taxonomy = sTaxonomy;

					sTaxonomy.terms = new HashSet<Term>();
					sTaxonomy.terms.add(term1);
					sTaxonomy.terms.add(term2);
					termRepository.save(term1);
					termRepository.save(term2);
					taxonomyRepository.save(sTaxonomy);
				}
            }
        }
		return Response.status(Status.OK).build();
    }

	@GET
	@Path("/updateStationTagsTaxonomy")
	@Transactional(readOnly=false)
	public Response updateStationTagsTaxonomy(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Station> stations = stationRepository.findAll();
			for (Station station: stations){
				Set<Taxonomy> taxonomies = station.ownedTaxonomies;
				boolean hasStationTag = false;
				for (Taxonomy tax: taxonomies){
					if(tax.type.equals(Taxonomy.STATION_TAG_TAXONOMY))
						hasStationTag = true;
				}
				if(!hasStationTag){
					//Station Default Taxonomy
					Taxonomy sTaxonomy = new Taxonomy();
					sTaxonomy.name = "Station: " + station.name;
					sTaxonomy.owningStation = station;
					sTaxonomy.type = Taxonomy.STATION_TAG_TAXONOMY;
					taxonomyRepository.save(sTaxonomy);
					station.ownedTaxonomies.add(sTaxonomy);
					stationRepository.save(station);
				}
			}
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateStationTagsCategoriesIds")
	@Transactional(readOnly=false)
	public Response updateStationTagsCategoriesIds(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Station> stations = stationRepository.findAll();
			for (Station station: stations){
				Set<Taxonomy> taxonomies = station.ownedTaxonomies;
				for (Taxonomy tax: taxonomies){
					if(tax.type.equals(Taxonomy.STATION_TAG_TAXONOMY)){
						if(station.tagsTaxonomyId == null)
							station.tagsTaxonomyId = tax.id;
					}

					if(tax.type.equals(Taxonomy.STATION_TAXONOMY)){
						if(station.categoriesTaxonomyId == null)
							station.categoriesTaxonomyId = tax.id;
					}
				}
			}
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateStationTerm")
	@Transactional(readOnly=false)
	public Response updateAllTerms(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Term> terms = termRepository.findAll();
			for(Term term: terms){
				term.taxonomyId = term.taxonomy.id;
				term.taxonomyName = term.taxonomy.name;
			}
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateAllResources")
	@Transactional(readOnly=false)
	public Response updateAllResources(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			//			reindexAll(request);
			//			recalculateSlug(request);
			updateDefaultStationPerspective(request);
			updatePostFields(request);
			updateTermPerspectivesStationIds(request);
			//updatePersonFields(request);
			updateRegIdsAndTokens(request);
			updateRegDate(request);
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateWordpressPosts")
	public Response updateWordpressPosts(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		int count = 0;

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> all = postRepository.findAllPostsOrderByIdDesc();
			List<Post> posts = new ArrayList<>();
			for (Post post : all) {
				if(post.wordpressId != null && post.featuredImage == null){
					WordpressParsedContent wpc = wordrailsService.extractImageFromContent(post.body, post.externalFeaturedImgUrl);
					post.body = wpc.content;
					post.featuredImage = wpc.image;
					post.externalFeaturedImgUrl = wpc.externalImageUrl;
					System.out.println(post.id + " " + (wpc.image != null ? wpc.image.id : "") + " " + post.externalFeaturedImgUrl);
					if(post.externalFeaturedImgUrl != null){
						count++;
						posts.add(post);
					}
				}
				if(count > 100)
					break;
			}

			postRepository.save(posts);
		}
		return Response.status(Status.OK).build();
	}

	private @Autowired PostReadRepository postReadRepository;
	private @Autowired CellRepository cellRepository;
	private @Autowired CommentRepository commentRepository;
	private @Autowired ImageRepository imageRepository;
	private @Autowired BookmarkRepository bookmarkRepository;
	private @Autowired RecommendRepository recommendRepository;
	private @Autowired NotificationRepository notificationRepository;

	@GET
	@Path("/removeWordpress")
	public Response removeWordpress(@Context HttpServletRequest request, @QueryParam("token") String token){
		int countPost = 0;
		int countTerm = 0;
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			Wordpress wp = wordpressRepository.findByToken(token);

			Station station = stationRepository.findByWordpressToken(token);

			if(station == null) {
				return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Something is very wrong:" + token).build();
			} else if (wp == null) {
				return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Token invalid:" + token).build();
			} 

			List<Post> posts = postRepository.findByStation(station);
			for (Post post : posts) {
				if(post.wordpressId != null){
					List<Image> images = imageRepository.findByPost(post);
					if (images != null && images.size() > 0) {
						postRepository.updateFeaturedImagesToNull(images);
					}
					imageRepository.delete(images);
					cellRepository.delete(cellRepository.findByPost(post));
					commentRepository.delete(post.comments);
					postReadRepository.deleteByPost(post);
					notificationRepository.deleteByPost(post);
					bookmarkRepository.deleteByPost(post);
					recommendRepository.deleteByPost(post);
					postRepository.delete(post);

					countPost++;
				}
			}
			Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
			Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
			List<Term> terms = new ArrayList<>();
			terms.addAll(termRepository.findByTaxonomy(tagTaxonomy));
			terms.addAll(termRepository.findByTaxonomy(categoryTaxonomy));
			for (Term term : terms) {
				if(term.wordpressId != null){
					countTerm += deleteCascade(term);
				}
			}
		}

		return Response.status(Response.Status.OK).type("text/plain").entity("Posts:"+countPost+" Terms:"+countTerm).build();
	}
	@Autowired private TermRepository termRepository;
	@Autowired private RowRepository rowRepository; 
	@Autowired private WordpressRepository wordpressRepository;

	@Transactional
	public int deleteCascade(Term term){
		int countTerm = 0;
		if(term.termPerspectives != null && term.termPerspectives.size() > 0){
			termPerspectiveRepository.delete(term.termPerspectives);
		}

		List<Row> rows = rowRepository.findByTerm(term);
		if(rows != null && rows.size() > 0){
			rowRepository.delete(rows);
		}

		List<Term> terms = termRepository.findByParent(term);
		if(terms != null && terms.size() > 0){
			for (Term t : terms) {
				countTerm += deleteCascade(t);
			}
		}

		try{
			termRepository.delete(term);
			countTerm++;
		} catch(JpaObjectRetrievalFailureException e) {

		}

		return countTerm;
	}

	@Autowired private PersonNetworkRegIdRepository personNetworkRegIdRepository; 

	@GET
	@Path("/updateRegDate")
	public Response updateRegDate(HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<PersonNetworkRegId> ids = personNetworkRegIdRepository.findAll();
			for (PersonNetworkRegId personNetworkRegId : ids) {
				personNetworkRegId.createdAt = new Date();
				personNetworkRegId.updatedAt = new Date();
			}
			personNetworkRegIdRepository.save(ids);
		}

		return Response.status(Status.OK).build();
	}

	@Autowired private QueryPersistence qp;
	@Autowired private PersonNetworkRegIdRepository reg;
	@Autowired private AsyncService asyncService; 

	@Autowired
	private Scheduler sched;

	@GET
	@Path("/test")
	public Response test(@Context HttpServletRequest request){
		String host = request.getHeader("Host");
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			reg.findRegIdByStationId(2);
			asyncService.test();
			String content = "<div class='text-black pt-serif blue ng-binding' ng-style='app.customStyle.secondaryFont' bind-html-unsafe='post.body' style='font-family: 'PT Serif', sans-serif;'>[caption id='attachment_66378' align='alignnone' width='770']<a href='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg'><img class='size-full wp-image-66378' src='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg' alt='Foto: Divulgação/FPF' width='770' height='416'></a> Foto: Divulgação/FPF[/caption] Marcelo de Lima Henrique é quem vai comandar o primeiro jogo da decisão entre Salgueiro e Santa Cruz, no Cornélio de Barros, na próxima quarta-feira, às 22h. Ele será auxiliado por Clóvis Amaral e Fernanda Colombo. Já a volta terá Emerson Sobral como árbitro principal ao lado de Albert Júnior e Elan Vieira. O segundo jogo será no domingo, às 16h, no Arruda. A Federação Pernambucana também definiu quem apita os jogos entre Sport e Central. Sebastião Rufino Filho comanda a primeira partida, no Lacerdão, enquanto Giorgio Wilton o segundo, na Ilha do Retiro.</div>";
			WordpressParsedContent wpc = wordrailsService.extractImageFromContent(content);
			System.out.println(wpc.content);
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/removeOldImages")
	@Transactional
	@Modifying
	public Response removeOldImages(@Context HttpServletRequest request) throws SchedulerException {
		String host = request.getHeader("Host");
		if (host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")) {
			DateTime dateTime = new DateTime().minusDays(30);

			List<Post> posts = manager.createQuery("select post from Post post where post.featuredImage is not null and date(post.date) < :dateTime AND post.state = 'PUBLISHED'").setParameter("dateTime",dateTime.toDate()).getResultList();
			List<Integer> ids = new ArrayList<Integer>();

			List<Image> images = new ArrayList<Image>();

			for(Post post: posts){
				images.add(post.featuredImage);
				ids.add(post.id);
			}

			List<Integer> imgIds = new ArrayList<Integer>();
			for(Image image: images){
				imgIds.add(image.id);
			}

			postRepository.updateFeaturedImagesToNull(images);

			imageRepository.deleteImages(imgIds);
		}

		return Response.status(Status.OK).build();
	}


	@GET
	@Path("/testQuartz")
	public Response testQuartz(@Context HttpServletRequest request) throws SchedulerException {
		String host = request.getHeader("Host");
		if (host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")) {
			JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

			Date runTime = DateBuilder.evenMinuteDate(new Date());
			// Trigger the job to run on the next round minute
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();

			sched.scheduleJob(job, trigger);
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateUserNetwork")
	public Response updateUserNetwork(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");
		if (host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1") || host.contains("xarxlocal.com")) {
			List<Person> persons = manager.createQuery("SELECT person FROM Person person JOIN FETCH person.user user JOIN FETCH user.network network").getResultList();
			for(Person person : persons){
				person.networkId = person.user.network.id;
				person.email = person.email != null ? person.email.trim().toLowerCase() : null;
				if(!Pattern.matches("^[a-z0-9\\._-]{3,50}$", person.username)){
					person.username = TrixUtil.generateRandomString(10, "a");
				}
			}
			personRepository.save(persons);
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("/deleteNetwork/{id}")
	public Response deleteNetwork (@Context HttpServletRequest request, @PathParam("id") Integer networkId) {
		String host = request.getHeader("Host");
		if (host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")) {

			Network network = networkRepository.findOne(networkId);

			List<NetworkRole> nr = personRepository.findNetworkAdmin(networkId);

			User user = nr.get(0).person.user;

			Set<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_NETWORK_ADMIN"));
			authProvider.passwordAuthentication(user.username, user.password, network);

			List<Station> stations = stationRepository.findByNetworkId(networkId);
			for (Station station: stations){
				stationEventHandler.handleBeforeDelete(station);
				stationRepository.delete(station);
			}

			List <Person> persons = personRepository.findAllByNetwork(networkId);
			for (Person person: persons){
				personEventHandler.handleBeforeDelete(person);
				personRepository.delete(person);
			}

			networkEventHandler.handleBeforeCreate(network);
			networkRepository.delete(network);

			cacheService.removeNetwork(networkId);
			cacheService.removeUser(user.id);
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("/removeRowFromPerspective/{perspectiveId}/{rowId}")
	public Response removeRowFromPerspective(@Context HttpServletRequest request, @PathParam("perspectiveId") Integer perspectiveId, @PathParam("rowId") Integer rowId) {
		String host = request.getHeader("Host");
		if (host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")) {

			Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

			List<NetworkRole> nr = personRepository.findNetworkAdmin(network.id);

			User user = nr.get(0).person.user;

			Set<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_NETWORK_ADMIN"));
			authProvider.passwordAuthentication(user.username, user.password, network);

			TermPerspective tp = termPerspectiveRepository.findPerspectiveAndTermNull(perspectiveId);

//			tp.rows.

			for(Row row: tp.rows){
				if(row.id.equals(rowId)){

				}
			}
		}
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/updateStationPerspectiveTaxonomyIds")
	public Response updateStationPerspectiveTaxonomyIds(@Context HttpServletRequest request) {
		if(isLocal(request.getHeader("Host"))) {
			List<StationPerspective> pers = stationPerspectiveRepository.findAll();
			for (StationPerspective per: pers){
				per.taxonomyId = per.taxonomy.id;
				per.taxonomyName = per.taxonomy.name;
				per.taxonomyType = per.taxonomy.type;
				if(per.perspectives != null)
				for(TermPerspective tper: per.perspectives){
					tper.taxonomyId = per.taxonomyId;
				}
				termPerspectiveRepository.save(per.perspectives);
			}
			stationPerspectiveRepository.save(pers);
		}
		return Response.status(Status.OK).build();
	}

	@Autowired
	private AmazonCloudService amazon;

	@POST
	@Path("/uploadAmazonImages")
	public Response uploadAmazonImages(@Context HttpServletRequest request) {
		if(isLocal(request.getHeader("Host"))) {
			amazon.uploadAmazonImages();
		}
		return Response.status(Status.OK).build();
	}

	private boolean isLocal(String host) {
		return host.contains("0:0:0:0:0:0:0") ||
				host.contains("0.0.0.0") ||
				host.contains("localhost") ||
				host.contains("127.0.0.1") ||
				host.contains("xarxlocal.com");
	}

    public Response adminAuth(Network network){
        List<NetworkRole> nr = personRepository.findNetworkAdmin(network.id);
        User user = nr.get(0).person.user;
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_NETWORK_ADMIN"));
        authProvider.passwordAuthentication(user.username, user.password, network);
        return Response.status(Status.OK).build();
    }

	@GET
	@Path("/updateRowPositions")
	@Transactional(readOnly=false)
	public void updateRowPositions(@Context HttpServletRequest request) throws InterruptedException {
		String host = request.getHeader("Host");

		if(isLocal(request.getHeader("Host"))){
			List<TermPerspective> tps = termPerspectiveRepository.findAll();
			for(TermPerspective tp: tps){
				List<Row> rows = rowRepository.findByPerspective(tp);
				Collections.sort(rows);
				int i = 0;
				for(Row row: rows){
					if(row.index == null){
						row.index = i;
					}
					i ++;
				}
				rowRepository.save(rows);
			}
		}
	}

	@GET
	@Path("/indexPostsToElastisearch")
	@Transactional(readOnly=false)
	public void indexPostsToElastisearch(@Context HttpServletRequest request) throws InterruptedException {
		String host = request.getHeader("Host");

		if(isLocal(request.getHeader("Host"))){
			List<Post> all = postRepository.findAllPostsOrderByIdDesc();
			for(int i = 0; i < all.size(); i++){
				postEsRepository.save(all.get(i));

				if(i % 50 == 0){
					Thread.sleep(100);
				}
			}
		}
	}

	@GET
	@Path("/indexPersonsToElastisearch")
	@Transactional(readOnly=false)
	public void indexPersonsToElastisearch(@Context HttpServletRequest request) throws InterruptedException {
		String host = request.getHeader("Host");

		if(isLocal(request.getHeader("Host"))){
			List<Person> all = personRepository.findAllPostsOrderByIdDesc();
			for(int i = 0; i < all.size(); i++){
				personRepository.save(all.get(i));

				if(i % 50 == 0){
					Thread.sleep(100);
				}
			}
		}
	}

	@GET
	@Path("/indexPerspectivesToElastisearch")
	@Transactional(readOnly=false)
	public void indexPerspectivesToElastisearch(@Context HttpServletRequest request) throws InterruptedException {
		String host = request.getHeader("Host");

		if(isLocal(request.getHeader("Host"))){
			List<TermPerspective> all = termPerspectiveRepository.findAll();
			for(int i = 0; i < all.size(); i++){
				perspectiveEsRepository.save(all.get(i));

				if(i % 50 == 0){
					Thread.sleep(100);
				}
			}
		}
	}

	@GET
	@Path("/indexBookmarksToElastisearch")
	@Transactional(readOnly=false)
	public void indexBookmarksToElastisearch(@Context HttpServletRequest request) throws InterruptedException {
		String host = request.getHeader("Host");

		if(isLocal(request.getHeader("Host"))){
			List<Bookmark> all = bookmarkRepository.findAll();
			for(int i = 0; i < all.size(); i++){
				bookmarkEsRespository.save(all.get(i));

				if(i % 50 == 0){
					Thread.sleep(100);
				}
			}
		}
	}


	@GET
	@Path("/deleteAllPerspectivesFromIndex")
	@Transactional(readOnly=false)
	public void deleteAllPerspectivesFromIndex(@Context HttpServletRequest request) throws InterruptedException {
		String host = request.getHeader("Host");

		if(isLocal(request.getHeader("Host"))){
			List<StationPerspective> all = stationPerspectiveRepository.findAll();
			for(int i = 0; i < all.size(); i++){
				perspectiveEsRepository.deleteByStationPerspective(all.get(i).id);

				if(i % 50 == 0){
					Thread.sleep(100);
				}
			}
		}
	}

	@Autowired
	private ImageScript imageScript;

	@POST
	@Path("/addPicturesToImages")
	@Transactional(readOnly=false)
	public void addPicturesToImages(@Context HttpServletRequest request) throws InterruptedException {
		if(isLocal(request.getHeader("Host"))){
			imageScript.addPicturesToImages();
		}
	}

}
