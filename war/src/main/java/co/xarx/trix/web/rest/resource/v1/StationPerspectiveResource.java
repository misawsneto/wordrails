package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.StationPerspectiveApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@NoArgsConstructor
public class StationPerspectiveResource extends AbstractResource implements StationPerspectiveApi {

	@Override
	public void postStationPerspective() throws IOException {
		forward();
	}

	@Override
	public void putStationPerspective(Integer id) throws IOException {
		forward();
	}
}
