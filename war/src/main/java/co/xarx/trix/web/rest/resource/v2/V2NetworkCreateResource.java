package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.NetworkCreate;
import co.xarx.trix.services.NetworkService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2NetworkCreateApi;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Slf4j
@Component
@NoArgsConstructor
public class V2NetworkCreateResource extends AbstractResource implements V2NetworkCreateApi {

//	private AuditService auditService;
//	private StatEventsService statEventsService;
//	private ElasticSearchService elasticSearchService;

	private NetworkService networkService;

	@Autowired
	public V2NetworkCreateResource(NetworkService networkService) {
		this.networkService = networkService;
	}

	@Override
	public Response postNetworkCreate(NetworkCreate networkCreate) throws ServletException, IOException {
		networkCreate.tenantId = TenantContextHolder.getCurrentTenantId();
		networkCreate.contacted = false;
		networkService.addNetworkCreateRequest(networkCreate);
		return Response.status(Response.Status.CREATED).build();
	}
}