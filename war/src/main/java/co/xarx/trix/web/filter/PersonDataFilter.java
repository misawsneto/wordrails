package co.xarx.trix.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.xarx.trix.web.rest.PersonsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PersonDataFilter implements Filter{
	
	private @Autowired
	PersonsResource personsResource;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	@Transactional(readOnly=true)
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		personsResource.getAllInitData((HttpServletRequest)req, (HttpServletResponse)res, true);
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}
	
	
}
