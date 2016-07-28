package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.services.StationService;
import co.xarx.trix.web.rest.api.v2.V2StationsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
public class V2StationsResource implements V2StationsApi {

	private StationService stationService;

	@Autowired
	public V2StationsResource(StationService stationService) {
		this.stationService = stationService;
	}

	@Override
	public Response getStations() throws IOException {
		return Response.ok().entity(stationService.findStations()).build();
	}
}
