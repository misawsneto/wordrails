package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.WordpressRepository;
import org.apache.commons.lang3.math.NumberUtils;
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
	private WordpressService wordpressService;

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
			log.info("POSTFILTER URL: " + rq.getMethod().toUpperCase() + " " + rq.getRequestURI());

			if (rq.getMethod().toLowerCase().equals("get")) {
				String url = rq.getRequestURI();
				Post post = null;
				if (url.contains("/posts/") && url.matches("(.*)\\d+$")) {
					postId = getPostId(url);
					if (postId != null)
						post = postRepository.findOne(postId);
				} else if (url.contains("findBySlug")) {
					String slug = rq.getParameter("slug");
					if (slug != null && !slug.isEmpty()) ;
					post = postRepository.findBySlug(slug);
				}
				if (post != null) {
					Wordpress wp = wordpressRepository.findByStation(post.station);
					handleAfterRead(post, wp);
					wordrailsService.countPostRead(post, authProvider.getLoggedPerson(), rq.getRequestedSessionId());
				}
			} else if (rq.getMethod().toLowerCase().equals("post") && res.containsHeader("Location")) {
				postId = getPostId(res.getHeader("Location"));
				Post post = postRepository.findOne(postId);
				Wordpress wp = wordpressRepository.findByStation(post.station);
				handleAfterCreate(post, wp);
			} else if (rq.getMethod().toLowerCase().equals("put") && NumberUtils.isNumber(urlParts[urlParts.length - 1])) {
				postId = getPostId(rq.getRequestURI());
				Post post = postRepository.findOne(postId);
				Wordpress wp = wordpressRepository.findByStation(post.station);
				handleAfterUpdate(post, wp);
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

	@Transactional
	private void handleAfterCreate(Post post, Wordpress wp) throws Exception {
		if (wp != null && wp.domain != null && wp.username != null && wp.password != null) {
			WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
			WordpressPost wpPost = wordpressService.createPost(post, api);
			post.wordpressId = wpPost.id;
			postRepository.save(post);
		}
	}

	@Transactional
	private void handleAfterUpdate(Post post, Wordpress wp) throws Exception {
		if (post != null) {
			if (wp != null && wp.domain != null && wp.username != null && wp.password != null) {
				WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
				wordpressService.updatePost(post, api);
			}
		}
	}

}
