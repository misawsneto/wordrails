package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class NetworkDomainFilter implements Filter {

	@Autowired
	private WordrailsService wordrailsService;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String host = request.getHeader("Host");

		if (host.equals("xarx.co") || host.equals("trix.rocks") || host.equals("xarxlocal.com")) {
			response.sendRedirect("/home");
		} else {
			Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
			if (network == null) {
				response.sendRedirect("/404.html");
				return;
			} else {
				//where should always enter in trix
				HttpSession session = request.getSession();
				session.setAttribute("network", network);
				session.setAttribute("networkId", network.id);
				session.setAttribute("networkSubdomain", network.subdomain);
			}
		}

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
