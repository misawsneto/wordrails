package com.wordrails.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.Network;
import com.wordrails.persistence.NetworkRepository;

@Component
public class NetworkDomainFilter implements Filter {

	@Autowired
	private NetworkRepository networkRepository;

	private @Autowired HttpServletRequest request;
	
	@Autowired
	private ServletContext ctx;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		String host = request.getHeader("Host");

		List<Network> networks = null;
		
		String build = (ctx != null ? ctx.getAttribute("buildNumber") : "") + "";
		
		if(host.equals("xarx.co")){
			((HttpServletResponse) res).sendRedirect("/home?" + build);
			return;
		}
		
		if(host.contains(".xarx.co")){
			String subdomain = host.split(".xarx.co")[0];
			networks = networkRepository.findBySubdomain(subdomain);
		}else if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			networks = networkRepository.findAll();
		}else{
			networks = networkRepository.findByDomain(host);
		}

		Network network = (networks != null && networks.size() > 0) ? networks.get(0) : networkRepository.findOne(1);
		HttpSession session = request.getSession();
		if(network!=null)
			session.setAttribute("network", network);

		HttpServletResponse resp = (HttpServletResponse) res;
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader ("Expires", 0);
		
		if(host.equals("xarx.co") || network == null){
			((HttpServletResponse) res).sendRedirect("/home");
		}

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}}