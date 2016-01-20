package co.xarx.trix.web.filter;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import java.util.Set;

@Component("networkDomainFilter")
public class NetworkDomainFilter implements Filter {

	@Autowired
	private NetworkRepository networkRepository;

	private Set<String> tenantIds;
	private Map<String, String> domains;

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@PostConstruct //method with no args is required
	public void init() throws ServletException {
		tenantIds = Sets.newConcurrentHashSet();
		domains = Maps.newConcurrentMap();

		List<Network> networks = networkRepository.findAll();
		for (Network n : networks) {
			tenantIds.add(n.tenantId);
			if(n.domain != null)
				domains.put(n.domain, n.tenantId);
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
			String subdomain = StringUtil.getSubdomainFromHost(host);
			if (domains.keySet().contains(host)) {
				TenantContextHolder.setCurrentTenantId(domains.get(host));
			} else if (tenantIds.contains(subdomain)) {
				TenantContextHolder.setCurrentTenantId(subdomain);
			} else {
				Network network = networkRepository.findByDomain(host);
				if (network != null) {
					TenantContextHolder.setCurrentTenantId(network.tenantId);
					domains.put(host, network.tenantId);
				} else {
					network = networkRepository.findBySubdomain(subdomain);
					if (network != null) {
						tenantIds.add(subdomain);
					} else {
						response.sendRedirect("/404.html");
						return;
					}
				}
			}

			HttpSession session = request.getSession();
			session.setAttribute("userAgent", request.getHeader("User-Agent"));
			session.setAttribute("tenantId", subdomain);

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

		request.getSession().setAttribute("userAgent", request.getHeader("User-Agent"));

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		chain.doFilter(req, res);
	}
}
