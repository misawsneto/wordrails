package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.services.analytics.RequestWrapper;
import co.xarx.trix.services.analytics.StatEventsService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2TermsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.ws.rs.core.Response;
import java.util.Date;

@Component
public class V2TermsApi extends AbstractResource implements V2TermsApi {

    private TermRepository termRepository;
    private StatEventsService statEventsService;

    @Autowired
    public V2TermsApi(TermRepository termRepository, StatEventsService statEventsService){
        this.termRepository = termRepository;
        this.statEventsService = statEventsService;
    }

    @Override
    public Response setTermSeen(Integer id, Integer timeReading, Long timestamp) {
        Term term = termRepository.findOne(id);
        Assert.notNull(term, "Term not found");
        RequestWrapper rw = new RequestWrapper(request);

        Date date = timestamp != null ? new Date(timestamp) : new Date();
        statEventsService.newTermView(term, rw, timeReading, date);
        return Response.ok().build();
    }
}
