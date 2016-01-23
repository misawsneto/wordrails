package co.xarx.trix.web.filter;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("networkDomainFilter")
public class NetworkDomainFilter implements Filter {

	private Map<String, Integer> tenantIds;
	private Map<String, Integer> domains;

	@Autowired
	private NetworkRepository networkRepository;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@PostConstruct //method with no args is required
	public void init() throws ServletException {
		tenantIds = Maps.newConcurrentMap();
		domains = Maps.newConcurrentMap();

		List<Network> networks = networkRepository.findAll();
		for (Network n : networks) {
			tenantIds.put(n.subdomain, n.id);
			if(n.domain != null)
				domains.put(n.domain, n.id);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String host = request.getHeader("Host");

		if (host.equals("xarx.co") || host.equals("trix.rocks") || host.equals("xarxlocal.com")) {
			response.sendRedirect("/home");
		} else {
			String tenantId = StringUtil.getSubdomainFromHost(host);
			if (domains.keySet().contains(host)) {
				TenantContextHolder.setCurrentNetworkId(domains.get(host));
			} else if (tenantIds.keySet().contains(tenantId)) {
				TenantContextHolder.setCurrentNetworkId(tenantIds.get(tenantId));
			} else {
				Network network = networkRepository.findByDomain(host);
				if (network != null) {
					TenantContextHolder.setCurrentNetworkId(network.id);
					domains.put(host, network.id);
				} else {
					network = networkRepository.findBySubdomain(tenantId);
					if (network != null) {
						tenantIds.put(tenantId, network.id);
					} else {
						response.sendRedirect("/404.html");
						return;
					}
				}
			}

			HttpSession session = request.getSession();
			session.setAttribute("userAgent", request.getHeader("User-Agent"));
			session.setAttribute("tenantId", tenantId);

//			Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
//			if (network == null) {
//				response.sendRedirect("/404.html");
//				return;
//			} else {
//				TenantContextHolder.setCurrentNetworkId(network.id);
//				TenantContextHolder.setCurrentTenantId(network.subdomain);
//				request.setAttribute("networkId", network.id);
//				//where should always enter in trix
//				session.setAttribute("network", network);
//				session.setAttribute("networkId", network.id);
//				session.setAttribute("networkSubdomain", network.subdomain);
//			}
		}

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		chain.doFilter(req, res);
	}
}
