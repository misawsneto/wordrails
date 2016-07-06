package co.xarx.trix.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("pathUrlFilter")
public class PathUrlFilter implements Filter {

	@Autowired
	private HttpServletRequest request;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		String path = request.getRequestURI();
		String originalPath = (String) request.getAttribute("originalPath");
		if(originalPath == null)
			request.setAttribute("originalPath", path);

		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {}
}
