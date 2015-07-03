package com.wordrails.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.WordrailsService;
import com.wordrails.business.*;
import com.wordrails.jobs.SimpleJob;
import com.wordrails.persistence.*;
import com.wordrails.services.AsyncService;
import com.wordrails.services.WordpressParsedContent;
import com.wordrails.util.WordrailsUtil;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.jboss.resteasy.spi.HttpRequest;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.*;

@Path("/util")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UtilResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired PersonRepository personRepository;

	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired PostRepository postRepository;
	
	private @Autowired TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;
	
	private @Autowired InvitationRepository invitationRepository;
	
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;

	private @PersistenceContext EntityManager manager;

	/**
	 * Method to manually update the Full Text Index. This is not required if inserting entities
	 * using this Manager as they will automatically be indexed. Useful though if you need to index
	 * data inserted using a different method (e.g. pre-existing data, or test data inserted via
	 * scripts or DbUnit).
	 */
	@GET
	@Path("/updateIndex")
	public Response updateFullTextIndex(@Context HttpServletRequest request) throws Exception {

		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){

			FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
			ftem.createIndexer().startAndWait();
			ftem.flushToIndexes();
			return Response.status(Status.OK).entity("Updated").build();
		}

		throw new UnauthorizedException();
	}

	/**
	 * Regenerates all the indexed class indexes
	 */
	@GET
	@Path("/reindex")
	public Response reindexAll(@Context HttpServletRequest request) {

		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			System.out.println("reindex started");
			FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
			MassIndexer massIndexer = ftem.createIndexer();
			massIndexer.purgeAllOnStart(true)
			.optimizeAfterPurge(true)
			.optimizeOnFinish(true)
			.batchSizeToLoadObjects( 30 )
			   .threadsToLoadObjects( 4 );
			//		massIndexer.start;
			try {
				massIndexer.startAndWait();
//				massIndexer.start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//		ftem.flushToIndexes();
			System.out.println("reindex finished");
			return Response.status(Status.OK).entity("Reindexed").build();
		}
		throw new UnauthorizedException();
	}

	@GET
	@Path("/updateDefaultStationPerspective")
	public void updateDefaultStationPerspective(@Context HttpServletRequest request) {
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
	}
	
	@GET
	@Path("/updatePostFields")
	public void updatePostFields(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> posts = postRepository.findAll();
			for (Post post : posts) {
				if(post.featuredImage != null && post.featuredImage.original != null){
					post.imageId = post.featuredImage.original.id;
					post.imageSmallId = post.featuredImage.small.id;
					post.imageMediumId = post.featuredImage.medium.id;
					post.imageLargeId= post.featuredImage.large.id;
					post.imageLandscape= !post.featuredImage.vertical;
					post.imageCaptionText = post.featuredImage.caption;
					post.imageCreditsText = post.featuredImage.credits;
				}else{
					post.imageId = null;
					post.imageSmallId = null;
					post.imageMediumId = null;
					post.imageLargeId = null;
				}
				if(post.comments != null){
					post.commentsCount = post.comments.size();
				}
				post.readTime = WordrailsUtil.calculateReadTime(post.body);
			}
			postRepository.save(posts);
		}
	}
	
	@GET
	@Path("/updateTermPerspectivesStationIds")
	public void updateTermPerspectivesStationIds(@Context HttpServletRequest request) {
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
	}
	
	@GET
	@Path("/updatePersonFields")
	public void updatePersonFields(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Person> persons = personRepository.findAll();
			for (Person person : persons) {
				if(person.image != null && person.image.original != null){
					person.imageId = person.image.original.id;
					person.imageSmallId = person.image.small.id;
					person.imageMediumId = person.image.medium.id;
					person.imageLargeId = person.image.large.id;
				}else{
					person.imageId = null;
					person.imageSmallId = null;
					person.imageMediumId = null;
					person.imageLargeId = null;
				}
				
				if(person.cover != null && person.cover.original != null){
					person.coverMediumId = person.cover.medium.id;
					person.coverLargeId = person.cover.large.id;
				}
			}
			
			personRepository.save(persons);
		}
	}
	
	@GET
	@Path("/recalculateSlug")
	public void recalculateSlug(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> posts = postRepository.findAll();
			for (Post post : posts) {
				doSlug(post);
			}
			postRepository.save(posts);
		}
	}
	
	@POST
	@Path("/generateInvitations")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void generate(@FormParam("subdomain") String subdomain, @FormParam("stationId") Integer stationId, @FormParam("count") Integer count){
		
		Network network = networkRepository.findOneBySubdomain(subdomain);
		Station station = stationId != null ? stationRepository.findOne(stationId) : null;
		
		List<Invitation> invites = new ArrayList<>();
		
		for (int i = 0; i < count; i++) {
			Invitation invitation =  new Invitation();
			invitation.network = network;
			invitation.station = station;
			invitation.active = true;
			invitation.hash = WordrailsUtil.generateRandomString(8, "aA#");
			invites.add(invitation);
		}
		
		invitationRepository.save(invites);
	}
	
	private void doSlug(Post post){
		String originalSlug = WordrailsUtil.toSlug(post.title);
		post.originalSlug = originalSlug;
		try {
			post.slug = originalSlug;
			postRepository.save(post);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			String hash = WordrailsUtil.generateRandomString(5, "Aa#");
			post.slug = originalSlug + "-" + hash;
		}
	}
	
	@GET
	@Path("/updateAllResources")
	public void updateAllResources(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
//			reindexAll(request);
			updateDefaultStationPerspective(request);
			updatePostFields(request);
			updateTermPerspectivesStationIds(request);
			updatePersonFields(request);
			recalculateSlug(request);
			updateRegDate(request);
		}
	}
	
	@GET
	@Path("/updateWordpressPosts")
	public void updateWordpressPosts(@Context HttpServletRequest request){
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
	}
    
	private @Autowired PostReadRepository postReadRepository;
	private @Autowired CellRepository cellRepository;
	private @Autowired CommentRepository commentRepository;
	private @Autowired ImageRepository imageRepository;
	private @Autowired PromotionRepository promotionRepository;
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
                    promotionRepository.delete(post.promotions);
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
	private void updateRegDate(HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<PersonNetworkRegId> ids = personNetworkRegIdRepository.findAll();
			for (PersonNetworkRegId personNetworkRegId : ids) {
				personNetworkRegId.createdAt = new Date();
				personNetworkRegId.updatedAt = new Date();
			}
			personNetworkRegIdRepository.save(ids);
		}
	}

	@Autowired private QueryPersistence qp;
	@Autowired private PersonNetworkRegIdRepository reg;
	@Autowired private AsyncService asyncService; 
	
	@Autowired
	private Scheduler sched;

	@GET
	@Path("/test")
	public void test(@Context HttpServletRequest request){
		String host = request.getHeader("Host");
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			reg.findRegIdByStationId(2);
			asyncService.test();
			String content = "<div class='text-black pt-serif blue ng-binding' ng-style='app.customStyle.secondaryFont' bind-html-unsafe='post.body' style='font-family: 'PT Serif', sans-serif;'>[caption id='attachment_66378' align='alignnone' width='770']<a href='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg'><img class='size-full wp-image-66378' src='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg' alt='Foto: Divulgação/FPF' width='770' height='416'></a> Foto: Divulgação/FPF[/caption] Marcelo de Lima Henrique é quem vai comandar o primeiro jogo da decisão entre Salgueiro e Santa Cruz, no Cornélio de Barros, na próxima quarta-feira, às 22h. Ele será auxiliado por Clóvis Amaral e Fernanda Colombo. Já a volta terá Emerson Sobral como árbitro principal ao lado de Albert Júnior e Elan Vieira. O segundo jogo será no domingo, às 16h, no Arruda. A Federação Pernambucana também definiu quem apita os jogos entre Sport e Central. Sebastião Rufino Filho comanda a primeira partida, no Lacerdão, enquanto Giorgio Wilton o segundo, na Ilha do Retiro.</div>";
			WordpressParsedContent wpc = wordrailsService.extractImageFromContent(content);
			System.out.println(wpc.content);
		}
	}

	@GET
	@Path("/testQuartz")
	public void testQuartz(@Context HttpServletRequest request) throws SchedulerException {
		String host = request.getHeader("Host");
		if (host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")) {
			JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

			Date runTime = DateBuilder.evenMinuteDate(new Date());
			// Trigger the job to run on the next round minute
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();

			sched.scheduleJob(job, trigger);
		}
	}
}
