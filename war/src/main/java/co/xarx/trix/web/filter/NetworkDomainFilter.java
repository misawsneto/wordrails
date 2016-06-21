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


		if (host == null) {
			throw new BadRequestException("Host is null");
		}

		if (host.equals("xarx.co") || host.equals("trix.rocks") || host.equals("xarxlocal.com")) {
			response.sendRedirect("/home");
		} else {
			String tenantId = networkService.getTenantFromHost(host);
			String currentTenantId = TenantContextHolder.getCurrentTenantId();
			if (currentTenantId != null && !currentTenantId.equals(tenantId)) {
				System.out.println("RED FLAG!!!! REQUEST IS FOR TENANT " + tenantId + " BUT IN SESSION WAS SET " + currentTenantId);
			}

			if (tenantId == null) {
				response.sendRedirect("/404.html");
				return;
			}

			HttpSession session = request.getSession();
			session.setAttribute("userAgent", request.getHeader("User-Agent"));
			session.setAttribute("tenantId", tenantId);
			TenantContextHolder.setCurrentTenantId(tenantId);
//			tenantProvider.setTenantId(tenantId);
		}

		chain.doFilter(req, res);
		TenantContextHolder.setCurrentTenantId(null);
	}
}
