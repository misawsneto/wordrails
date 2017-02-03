package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.api.v2.UserPermissionData;
import co.xarx.trix.domain.ESstatEvent;
import co.xarx.trix.domain.page.query.statement.PersonStatement;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.TimelineService;
import co.xarx.trix.services.person.PersonSearchService;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.util.SpringDataUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PersonsApi;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Component
public class V2PersonsResource extends AbstractResource implements V2PersonsApi {

	private PersonService personService;
	private TimelineService timelineService;
	private PersonSearchService personSearchService;
	private PersonPermissionService personPermissionService;

	@Autowired
	public V2PersonsResource(PersonService personService, TimelineService timelineService, PersonSearchService personSearchService, PersonPermissionService personPermissionService) {
		this.personService = personService;
		this.timelineService = timelineService;
		this.personSearchService = personSearchService;
		this.personPermissionService = personPermissionService;
	}

	@Override
	public Response searchPersons(String query,
								  List<String> usernames,
								  List<String> emails,
								  Integer page,
								  Integer size,
								  List<String> orders,
								  List<String> embeds) {

		PersonStatement params = new PersonStatement(query, usernames, emails, orders);

		List<PersonData> data = personSearchService.search(params, page, size);

		Set<String> postEmbeds = Sets.newHashSet("twitter");

		super.removeNotEmbeddedData(embeds, data, PersonData.class, postEmbeds);

		Pageable pageable = SpringDataUtil.getPageable(page, size, orders);
		Page p = new PageImpl(data, pageable, data.size());

		return Response.ok().entity(p).build();
	}

	@Override
	public Response getPermissions(String username, Integer stationId) {
		UserPermissionData data = personPermissionService.getPermissions(new PrincipalSid(username), stationId);

		return Response.ok().entity(data).build();
	}

	@Override
	public Response getPermissions(String username) {
		UserPermissionData data = personPermissionService.getPermissions(new PrincipalSid(username));
		return Response.ok().entity(data).build();
	}

	@Override
	public Response deletePerson(String email) {
		try {
			personService.deletePerson(email);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Override
	public Response getPersonTimeline(String username) {
		List<ESstatEvent> timeline = timelineService.getUserTimeline(username);

		if(timeline.isEmpty()){
			return Response.noContent().build();
		} else {
			return Response.ok()
					.entity(timeline)
					.build();
		}

	}

	@Override
	public Response notifyStations(StationsDto stationsDto) {
		return null;
	}

	@Override
	public Response notifyAllStaitons() {
		return null;
	}
}
