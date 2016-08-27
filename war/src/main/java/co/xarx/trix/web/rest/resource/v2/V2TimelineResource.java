package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.PersonTimelineData;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.TimelineService;
import co.xarx.trix.web.rest.api.v2.V2TimelineApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component
public class V2TimelineResource implements V2TimelineApi {
	private TimelineService timelineService;

	@Autowired
	public V2TimelineResource(TimelineService timelineService){
        this.timelineService = timelineService;
	}

	@Override
	public Response getPersonTimeline(String username) {
		PersonTimelineData timeline = timelineService.getUserTimeline(username);

		if(timeline.events.isEmpty()){
			return Response.noContent().build();
		} else {
			return Response.ok()
					.entity(timeline)
					.build();
		}

	}
}
