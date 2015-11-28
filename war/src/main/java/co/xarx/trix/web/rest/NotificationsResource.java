package co.xarx.trix.web.rest;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.converter.NotificationConverter;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("/notifications")
@Consumes(MediaType.WILDCARD)
@Component
public class NotificationsResource {
	private @Context HttpServletRequest request;
	private @Context UriInfo uriInfo;
	private @Context HttpServletResponse response;
	private @Autowired
	NotificationRepository notificationRepository;
	private @Autowired
	TrixAuthenticationProvider authProvider;
	
	private @Autowired
	NotificationConverter notificationConverter;

	@GET
	@Path("/searchNotifications")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<NotificationView>> searchNotifications(@QueryParam("query") String query, @QueryParam("page") Integer page, @QueryParam("size") Integer size){

		Person person = authProvider.getLoggedPerson();
//		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//		Network network = wordrailsService.getNetworkFromHost(request);
//
//		PermissionId pId = new PermissionId();
//		pId.baseUrl = baseUrl;
//		pId.networkId = network.id;
//		pId.personId = person.id;
//
//		StationsPermissions permissions = new StationsPermissions();
//		try {
//			permissions = wordrailsService.getPersonPermissions(pId);
//		} catch (ExecutionException e1) {
//			e1.printStackTrace();
//		}
//
//		List<Integer> readableIds = wordrailsService.getReadableStationIds(permissions);

//		if(query == null || query.trim().isEmpty()){
			Pageable pageable = new PageRequest(page, size);

			ContentResponse<List<NotificationView>> response = new ContentResponse<List<NotificationView>>();
			List<Notification> pages = notificationRepository.findNotificationsByPersonIdOrderByDate(person.id, pageable);

			List<NotificationView> notifications = new ArrayList<>();
			for (Notification notification : pages) {
				notifications.add(notificationConverter.convertToView(notification));
			}
			response.content = notifications;
			return response;
//		}

//		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
//		// create native Lucene query unsing the query DSL
//		// alternatively you can write the Lucene query using the Lucene query parser
//		// or the Lucene programmatic API. The Hibernate Search DSL is recommend though
//		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Notification.class).get();
//
//		org.apache.lucene.search.Query text = null;
//		try{
//
//			text = qb.keyword()
//				.fuzzy()
//				.withThreshold(.8f)
//				.withPrefixLength(1)
//				.onField("post.title").boostedTo(5)
//				.andField("post.body").boostedTo(2)
//				.andField("post.topper")
//				.andField("post.subheading")
//				.andField("post.author.name")
//				.andField("post.terms.name")
//				.matching(query).createQuery();
//		}catch(Exception e){
//
//			e.printStackTrace();
//
//			ContentResponse<List<NotificationView>> response = new ContentResponse<List<NotificationView>>();
//			response.content = new ArrayList<NotificationView>();
//
//			return response;
//		};

//		org.apache.lucene.search.Query personQuery = qb.keyword().onField("person.id").ignoreAnalyzer().matching(person.id).createQuery();

//		BooleanJunction stations = qb.bool();
//		for (Integer integer : readableIds) {
//			stations.should(qb.keyword().onField("post.station.id").ignoreAnalyzer().matching(integer).createQuery());
//		}
//
//		org.apache.lucene.search.Query full = qb.bool().must(text).must(personQuery).must(stations.createQuery()).createQuery();

//		org.apache.lucene.search.Query full = qb.bool().must(text).must(personQuery).createQuery();

//		FullTextQuery ftq = ftem.createFullTextQuery(full, Notification.class);
//		org.apache.lucene.search.Sort sort = new Sort( SortField.FIELD_SCORE, new SortField("id", SortField.INT, true));
//		ftq.setSort(sort);
//
//		// wrap Lucene query in a javax.persistence.Query
//		javax.persistence.Query persistenceQuery = ftq;
//
//		// execute search
//		List<Notification> result = persistenceQuery
//				.setFirstResult(size * page)
//				.setMaxResults(size)
//				.getResultList();
//
//		List<NotificationView> notifications = new ArrayList<NotificationView>();
//		for (Notification notification : result) {
//			notifications.add(notificationConverter.convertToView(notification));
//		}
//
//		ContentResponse<List<NotificationView>> response = new ContentResponse<List<NotificationView>>();
//		response.content = notifications;
//
//		return response;
	}
//
}