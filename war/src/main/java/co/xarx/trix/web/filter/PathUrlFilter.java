package co.xarx.trix.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component("pathUrlFilter")
public class PathUrlFilter implements Filter {

	@Autowired
	private HttpServletRequest request;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	private static final Set<String> APPLIED_PATHS = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList("", "/api", "/index.jsp", "/settings.jsp")));

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String host = request.getHeader("Host");
		String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
		boolean appliedPath = APPLIED_PATHS.contains(path) || APPLIED_PATHS.stream().anyMatch(p ->
				path != null && p != null &&
				!p.equals("") &&
				!path.equals("") &&
				path.startsWith(p));

		String originalPath = (String) request.getAttribute("originalPath");
		if(originalPath == null && appliedPath)
			request.setAttribute("originalPath", path);

		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {}
}
