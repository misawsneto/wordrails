package co.xarx.trix.config;

import java.io.IOException;

import co.xarx.trix.domain.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;

public class ResponseErrorHandler implements org.springframework.web.client.ResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return RestUtil.isError(response.getStatusCode());
    }
}