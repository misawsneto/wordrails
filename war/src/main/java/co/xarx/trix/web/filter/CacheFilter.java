package co.xarx.trix.web.filter;

import co.xarx.trix.services.NetworkService;
import co.xarx.trix.util.Constants;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("cacheFilter")
public class CacheFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		if(NetworkService.getDeviceFromRequest(httpServletRequest) != Constants.MobilePlatform.ANDROID) {
			httpServletResponse.setHeader("Cache-Control", "public, no-store, no-cache, must-revalidate, max-age=0, " +
					"post-check=0, pre-check=0");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setDateHeader("Expires", 0);
		}else{
			httpServletResponse.setHeader("Cache-Control", httpServletRequest.getHeader("Cache-Control"));
			httpServletResponse.setHeader("Pragma", "cache");
			httpServletResponse.setDateHeader("Expires", httpServletRequest.getDateHeader("Expires"));
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
}