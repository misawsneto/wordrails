package co.xarx.trix.config.multitenancy;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.domain.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInstanceInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private WordrailsService wordrailsService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!(handler instanceof ResourceHttpRequestHandler)) {
			Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
			TenantContextHolder.setCurrentTenantId(network.getTenantId());
		}

		return super.preHandle(request, response, handler);
	}
}
