package com.wordrails.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Image;
import com.wordrails.business.Invitation;
import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkRegId;
import com.wordrails.business.Post;
import com.wordrails.business.Row;
import com.wordrails.business.Station;
import com.wordrails.business.StationPerspective;
import com.wordrails.business.Taxonomy;
import com.wordrails.business.Term;
import com.wordrails.business.TermPerspective;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.business.Wordpress;
import com.wordrails.persistence.BookmarkRepository;
import com.wordrails.persistence.CellRepository;
import com.wordrails.persistence.CommentRepository;
import com.wordrails.persistence.FavoriteRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.InvitationRepository;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.NotificationRepository;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostReadRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.PromotionRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.RecommendRepository;
import com.wordrails.persistence.RowRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermPerspectiveRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.persistence.WordpressRepository;
import com.wordrails.util.AsyncService;
import com.wordrails.util.WordpressParsedContent;
import com.wordrails.util.WordrailsUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Path("/util")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UtilResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired UserDetailsManager userDetailsManager;
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
			return Response.status(Status.OK).entity("Updating...").build();
		}

		throw new UnauthorizedException();
	}

	/**
	 * Regenerates all the indexed class indexes
	 *
	 * @param async true if the reindexing will be done as a background thread
	 * @param sess the hibernate session
	 */
	@GET
	@Path("/reindex")
	public Response reindexAll(@Context HttpServletRequest request) {

		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
			MassIndexer massIndexer = ftem.createIndexer();
			massIndexer.purgeAllOnStart(true);
			//		massIndexer.start;
			try {
				massIndexer.startAndWait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//		ftem.flushToIndexes();
			return Response.status(Status.OK).entity("Reindexing...").build();
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
		
		List<Invitation> invites = new ArrayList<Invitation>();
		
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
			String hash = WordrailsUtil.generateRandomString(5, "Aa#u");
			post.slug = originalSlug + "-" + hash;
		}
	}
	
	@GET
	@Path("/updateAllResources")
	public void updateAllResources(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			reindexAll(request);
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
			List<Post> posts = new ArrayList<Post>();
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
	private @Autowired FavoriteRepository favoriteRepository;
	private @Autowired BookmarkRepository bookmarkRepository;
	private @Autowired RecommendRepository recommendRepository;
	private @Autowired NotificationRepository notificationRepository;
	
	@GET
	@Path("/removeWordpressPosts")
	public void removeWordpressPosts(@Context HttpServletRequest request, @FormParam("stationId") Integer stationId){
		String host = request.getHeader("Host");
		
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
            Station station = stationRepository.findOne(stationId);
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
                    favoriteRepository.deleteByPost(post);
                    bookmarkRepository.deleteByPost(post);
                    recommendRepository.deleteByPost(post);
                    postRepository.delete(post);
				}
			}
		}
	
    }
	@Autowired private TermRepository termRepository;
	@Autowired private RowRepository rowRepository; 
    @Autowired private WordpressRepository wordpressRepository;
	
	@GET
	@Path("/removeWordpressTerms")
	public void removeWordpressTerms(@Context HttpServletRequest request, @FormParam("wpToken") String wpToken){
		String host = request.getHeader("Host");
		
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
            Wordpress wp = wordpressRepository.findByToken(wpToken);
            Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
            Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
            List<Term> terms = new ArrayList<>();
			terms.addAll(termRepository.findByTaxonomy(tagTaxonomy));
			terms.addAll(termRepository.findByTaxonomy(categoryTaxonomy));
			for (Term term : terms) {
				if(term.wordpressId != null){
                    deleteCascade(term, term);
                    termRepository.delete(term);
				}
			}
		}
	}
    
    @Transactional
    public void deleteCascade(Term termToDelete, Term term){
		if(term.termPerspectives != null && term.termPerspectives.size() > 0){
			termPerspectiveRepository.delete(term.termPerspectives);
		}
		
		List<Row> rows = rowRepository.findByTerm(term);
		if(rows != null && rows.size() > 0){
			rowRepository.delete(rows);
		}

		List<Term> terms = termRepository.findByParent(term);
		if(terms != null && terms.size() > 0){
			deleteCascade(termToDelete, terms);
		}
		termRepository.deletePostsTerms(term.id);
		if(!termToDelete.equals(term)){
			termRepository.delete(term);
		}
	}
	
    @Transactional
	private void deleteCascade(Term termToDelete, List<Term> terms){
		for (Term term : terms) {
			deleteCascade(termToDelete, term);
		}
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
	
	@GET
	@Path("/test")
	public void test(@Context HttpServletRequest request){
		String host = request.getHeader("Host");
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			//reg.findRegIdByStationId(2);
//			asyncService.test();
//			String content = "<div class='text-black pt-serif blue ng-binding' ng-style='app.customStyle.secondaryFont' bind-html-unsafe='post.body' style='font-family: 'PT Serif', sans-serif;'>[caption id='attachment_66378' align='alignnone' width='770']<a href='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg'><img class='size-full wp-image-66378' src='http://cockpitblogs.ne10.com.br/torcedor/wp-content/uploads/2015/04/marcelo-770.jpg' alt='Foto: Divulgação/FPF' width='770' height='416'></a> Foto: Divulgação/FPF[/caption] Marcelo de Lima Henrique é quem vai comandar o primeiro jogo da decisão entre Salgueiro e Santa Cruz, no Cornélio de Barros, na próxima quarta-feira, às 22h. Ele será auxiliado por Clóvis Amaral e Fernanda Colombo. Já a volta terá Emerson Sobral como árbitro principal ao lado de Albert Júnior e Elan Vieira. O segundo jogo será no domingo, às 16h, no Arruda. A Federação Pernambucana também definiu quem apita os jogos entre Sport e Central. Sebastião Rufino Filho comanda a primeira partida, no Lacerdão, enquanto Giorgio Wilton o segundo, na Ilha do Retiro.</div>";
//			WordpressParsedContent wpc = wordrailsService.extractImageFromContent(content);
//			System.out.println(wpc);
		}
	}
}
