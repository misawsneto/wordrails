package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.services.PersonService;
import co.xarx.trix.web.rest.api.v2.V2TimelineApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component
public class V2TimelineResource implements V2TimelineApi {

	@Autowired
	private PersonService personService;

//	@Autowired
//	public V2TimelineResource(PersonService personService){
//		this.personService = personService;
//	}

	@Override
	public Response getPersonTimeline(String username) {
		List<Map<String, Object>> timeline = personService.getUserTimeline(username);

		if(timeline.isEmpty()){
			return Response.noContent().build();
		} else {
			return Response.ok()
					.entity(timeline)
					.build();
		}

	}
}
