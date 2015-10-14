package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import com.wordrails.elasticsearch.PostEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PathEntityFilter implements Filter{

	private @Autowired PostEsRepository postEsRepository;
	private @Autowired WordrailsService wordrailsService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;

		String path = request.getRequestURI();

		String host = request.getHeader("Host");

		if(!path.equals("/") && path.split("/").length <= 2 &&
			!path.equals("/stations") &&
			!path.equals("/notifications") &&
			!path.equals("/bookmarks") &&
			!path.equals("/post") &&
			!path.equals("/settings") &&
			!path.equals("/search") &&
			!path.equals("/index.jsp") &&
			!path.equals("/mystats") &&
			!path.contains("/@")) {

			Network network = wordrailsService.getNetworkFromHost(host);
//			network.stations
			postEsRepository.findBySlug(path.replace("/",""));
		}

		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}
	
	
}
