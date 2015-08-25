package com.wordrails.api;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wordrails.business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import com.wordrails.persistence.NetworkRepository;

@Component
public class NetworkDomainFilter implements Filter {

	@Autowired
	private WordrailsService wordrailsService;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String host = request .getHeader("Host");

		if(host.equals("xarx.co") || host.equals("trix.rocks") || host.equals("xarxlocal.com")){
			((HttpServletResponse) res).sendRedirect("/home");
		}else {
			Network network = wordrailsService.getNetworkFromHost(req);
			if(network == null){
				((HttpServletResponse)res).sendRedirect("/404.html");
				return;
			}else{
				HttpSession session = request.getSession();
				if (network != null)
					session.setAttribute("network", network);
			}
		}

		HttpServletResponse resp = (HttpServletResponse) res;
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader ("Expires", 0);

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}}
