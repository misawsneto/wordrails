package co.xarx.trix.web.filter;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Wordpress;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.WordpressRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PostFilter implements Filter {

	static Logger log = Logger.getLogger(PostFilter.class.getName());

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private WordpressRepository wordpressRepository;

	@Autowired
	private WordrailsService wordrailsService;

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
		// before filter are applied
		fc.doFilter(req, resp);
		// after filters are applied
		HttpServletRequest rq = (HttpServletRequest) req;
		HttpServletResponse res = (HttpServletResponse) resp;
		Integer postId;
		try {
			String[] urlParts = rq.getRequestURI().split("/");
//			log.info("POSTFILTER URL: " + rq.getMethod().toUpperCase() + " " + rq.getRequestURI());

			if (rq.getMethod().toLowerCase().equals("get")) {
				String url = rq.getRequestURI();
				Post post = null;
				if (url.contains("/posts/") && url.matches("(.*)\\d+$")) {
					postId = getPostId(url);
					if (postId != null)
						post = postRepository.findOne(postId);
				} else if (url.contains("/findBySlug")) {
					String slug = rq.getParameter("slug");
					if (slug != null && !slug.isEmpty()) ;
					post = postRepository.findBySlug(slug);
				}
				if (post != null) {
					Wordpress wp = wordpressRepository.findByStation(post.station);
					handleAfterRead(post, wp);
					wordrailsService.countPostRead(post, authProvider.getLoggedPerson(), rq.getRequestedSessionId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	private void handleAfterRead(Post post, Wordpress wp) throws Exception {
		//not implemented
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
