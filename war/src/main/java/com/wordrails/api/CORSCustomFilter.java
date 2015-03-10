package com.wordrails.api;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSCustomFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse resp = (HttpServletResponse) res;
		HttpServletRequest reqt = (HttpServletRequest) req;
		String method = reqt.getMethod().toLowerCase();
		
		if(method.equals("get") || method.equals("post") || method.equals("put") || method.equals("head") || method.equals("options"))
			setCorsHeader(reqt, resp);
		if(method.equals("head") || method.equals("options")){
		    resp.setStatus(HttpServletResponse.SC_OK);
		    return;
		}
		
		chain.doFilter(req, res);
	}
	
	private void setCorsHeader(HttpServletRequest reqt, HttpServletResponse resp){
		String origin = reqt.getHeader("Origin");
		if(origin == null || origin.isEmpty())
			origin = "*";
		
		resp.setHeader("Access-Control-Allow-Origin", origin);
		resp.setHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,Content-Range,Content-Disposition,Content-Description,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
		resp.setHeader("Access-Control-Allow-Methods", "GET,POST,HEAD,OPTIONS,PUT");
		resp.setHeader("P3P", "CP=\"CAO PSA CONi OTR OUR DEM ONL\"");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
