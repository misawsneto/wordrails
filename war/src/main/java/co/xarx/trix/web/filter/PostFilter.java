package co.xarx.trix.web.filter;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("postFilter")
public class PostFilter implements Filter {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private AsyncService asyncService;

	@Autowired
	TrixAuthenticationProvider authProvider;

	@Override
	public void destroy() {/* not implemented */

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {/*
	 * not
	 * implemented
	 */
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException {
		fc.doFilter(req, resp);
		HttpServletRequest rq = (HttpServletRequest) req;
		Integer postId;
		try {
			if (rq.getMethod().toLowerCase().equals("get")) {
				String url = rq.getRequestURI();
				Post post = null;
				if (url.contains("/posts/") && url.matches("(.*)\\d+$")) {
					postId = getPostId(url);
					post = postRepository.findOne(postId);
				} else if (url.contains("/findBySlug")) {
					String slug = rq.getParameter("slug");
					post = postRepository.findBySlug(slug);
				}
				if (post != null) {
					asyncService.countPostRead(TenantContextHolder.getCurrentTenantId(), post,
							authProvider.getLoggedPerson(), rq.getRequestedSessionId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Spring data rest adds a Location header with hyperlink representation of the newly created post
	 *
	 * @param location
	 */
	private Integer getPostId(String location) throws Exception {
		String[] strings = location.split("/");
		return Integer.parseInt(strings[strings.length - 1]);
	}

}
