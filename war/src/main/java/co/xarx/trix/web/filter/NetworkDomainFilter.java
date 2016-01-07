package co.xarx.trix.web.filter;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
		tenantIds = Sets.newConcurrentHashSet(networkRepository.findTenantIds());
//		domains = new ConcurrentHashMap<>(networkRepository.findDomains());
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
			if (tenantIds.contains(subdomain)) {
				TenantContextHolder.setCurrentTenantId(subdomain);
			} else if(!domains.get(host).isEmpty()) {
				TenantContextHolder.setCurrentTenantId(domains.get(host));
			} else {
				response.sendRedirect("/404.html");
				return;
			}
		}

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		chain.doFilter(req, res);
	}
}
