package com.wordrails.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.elasticsearch.PostEsRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.util.TrixUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class PathEntityFilter implements Filter {

	private
	@Autowired
	PostEsRepository postEsRepository;
	private
	@Autowired
	PostRepository postRepository;
	private
	@Autowired
	WordrailsService wordrailsService;
	private
	@Autowired
	TrixAuthenticationProvider authenticationProvider;

	private
	@Autowired
	HttpServletRequest request;
	private
	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	private
	@Autowired
	AmazonCloudService amazonCloudService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		String path = request.getRequestURI();

		String host = request.getHeader("Host");

		if (!path.equals("/") && path.split("/").length <= 2 &&
				!path.equals("/stations") &&
				!path.equals("/notifications") &&
				!path.equals("/bookmarks") &&
				!path.equals("/post") &&
				!path.equals("/settings") &&
				!path.equals("/search") &&
				!path.equals("/index.jsp") &&
				!path.equals("/mystats") &&
				!path.contains("/@")) {

			Network network = wordrailsService.getNetworkFromHost(host);
			Person person = authenticationProvider.getLoggedPerson();

			Post post = postRepository.findBySlug(path.replace("/", ""));
//			List<Post> post = postEsRepository.findBySlug(path.replace("/",""));

			TrixUtil.EntityType entityType = TrixUtil.EntityType.POST;

			if (post != null) {
				request.setAttribute("requestedEntityJson", objectMapper.writeValueAsString(post));
				request.setAttribute("requestedEntityMetas", metaTagsBuilder(network.subdomain, post));
				request.setAttribute("requestedEntityHiddenHtml", hiddenHtmlBuilder(network.subdomain, post));
				request.setAttribute("entityType", entityType);
			}
		}

		chain.doFilter(req, res);
	}

	public String metaTagsBuilder(String subdomain, Post post) throws IOException {
		String html = "";

		html = html + "<meta property=\"og:url\" content=\"" + request.getRequestURL() + "\" />";
		html = html + "<meta property=\"og:title\" content=\"" + post.title + "\" />";
		html = html + "<meta property=\"og:description\" content=\"" + TrixUtil.simpleSnippet(post.body, 100) + "\" />";
		if (post.imageLargeHash != null)
			html = html + "<meta property=\"og:image\" content=\"" + amazonCloudService.getPublicImageURL(subdomain, post.imageLargeHash) + "\" />";

		return html;
	}

	public String hiddenHtmlBuilder(String subdomain, Post post) throws IOException {
		String html = "";

		if (post.imageLargeHash != null)
			html = html + "<img class=\"hidden\" src=\"" + amazonCloudService.getPublicImageURL(subdomain, post.imageLargeHash) + "\" />";
		html = html + "<h1 class=\"hidden\">" + post.title + "</h1>";
		html = html + "<div class=\"hidden\">" + post.body + "</div>";

		return html;
	}

	@Override
	public void destroy() {
	}


}
