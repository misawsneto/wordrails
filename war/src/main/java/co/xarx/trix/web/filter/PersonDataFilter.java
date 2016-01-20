package co.xarx.trix.web.filter;

import co.xarx.trix.web.rest.PersonsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("personDataFilter")
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
