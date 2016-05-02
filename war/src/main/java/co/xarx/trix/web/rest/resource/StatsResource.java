package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.StatsApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@NoArgsConstructor
public class StatsResource extends AbstractResource implements StatsApi {

	@Override
	public StatsData personStats(String date) throws IOException {
		return null;
	}

	@Override
	public StatsData postStats(String date, String beginning, Integer postId) throws IOException {
		return null;
	}

	@Override
	public StatsData authorStats(String date, String beginning, Integer authorId) throws IOException {
		return null;
	}

	@Override
	public Response networkStats(String date, String beginning) throws IOException {
		return null;
	}

	@Override
	public Response stationStats(String date, String beginning, Integer stationId) throws IOException {
		return null;
	}
}
