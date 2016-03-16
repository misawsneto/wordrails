package co.xarx.trix.web.filter;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.services.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component("networkDomainFilter")
public class NetworkDomainFilter implements Filter {

	@Autowired
	private NetworkService networkService;

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String host = request.getHeader("Host");

		if(host == null) {
			throw new BadRequestException("Host is null");
		}

		if (host.equals("xarx.co") || host.equals("trix.rocks") || host.equals("xarxlocal.com")) {
			response.sendRedirect("/home");
		} else {
			String tenantId = networkService.getTenantFromHost(host);

			if (tenantId == null) {
				response.sendRedirect("/404.html");
				return;
			}

			HttpSession session = request.getSession();
			session.setAttribute("userAgent", request.getHeader("User-Agent"));
			session.setAttribute("tenantId", tenantId);
			TenantContextHolder.setCurrentTenantId(tenantId);
		}

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		chain.doFilter(req, res);
	}
}
