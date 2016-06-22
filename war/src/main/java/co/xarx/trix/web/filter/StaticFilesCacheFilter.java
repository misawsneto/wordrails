package co.xarx.trix.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("staticFilesCacheFilter")
public class StaticFilesCacheFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		httpServletResponse.setHeader("Pragma", "cache");
		httpServletResponse.setHeader("Cache-control", "Public, max-age=31536000");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
}