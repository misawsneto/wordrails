package com.wordrails.api;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonDataFilter implements Filter{
	
	private @Autowired PersonsResource personsResource;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		
		PersonData personData = personsResource.getInitialData(request);
		HttpSession session = request.getSession();
		
		session.setAttribute("personData", personsResource.mapper.writeValueAsString(personData));
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}
	
	
}
