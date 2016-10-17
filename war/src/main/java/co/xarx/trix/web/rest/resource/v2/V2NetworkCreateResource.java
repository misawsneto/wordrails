package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.services.AuditService;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.analytics.StatEventsService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2NetworkCreateApi;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
@Component
@NoArgsConstructor
public class V2NetworkCreateResource extends AbstractResource implements V2NetworkCreateApi {

	private AuditService auditService;
	private StatEventsService statEventsService;
	private ElasticSearchService elasticSearchService;

	@Autowired
	public V2NetworkCreateResource(AuditService auditService, StatEventsService statEventsService, ElasticSearchService elasticSearchService) {
		this.auditService = auditService;
		this.statEventsService = statEventsService;
		this.elasticSearchService = elasticSearchService;
	}

	@Override
	public void postNetworkCreate() throws ServletException, IOException {
		forward();
	}
}