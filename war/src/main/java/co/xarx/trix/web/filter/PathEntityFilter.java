package co.xarx.trix.web.filter;

import co.xarx.trix.domain.Post;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("pathEntityFilter")
public class PathEntityFilter implements Filter {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private AmazonCloudService amazonCloudService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		String path = request.getRequestURI();

		if (!path.equals("/") && path.split("/").length <= 2 &&
				!path.equals("/stations") &&
				!path.equals("/notifications") &&
				!path.equals("/bookmarks") &&
				!path.equals("/post") &&
				!path.equals("/login") &&
				!path.equals("/settings") &&
				!path.equals("/search") &&
				!path.equals("/index.jsp") &&
				!path.equals("/mystats") &&
				!path.contains("/@")) {

//			Post post = postRepository.findBySlug(path.replace("/", ""));
//
//			if (post != null) {
//				request.setAttribute("requestedEntityJson", objectMapper.writeValueAsString(post));
//				request.setAttribute("requestedEntityMetas", metaTagsBuilder(post));
//				request.setAttribute("requestedEntityHiddenHtml", hiddenHtmlBuilder(post));
//				request.setAttribute("entityType", "POST");
//			}
		}

		chain.doFilter(req, res);
	}

	public String metaTagsBuilder(Post post) throws IOException {
		String html = "";

		html = html + "<meta property=\"og:url\" content=\"" + request.getRequestURL() + "\" />";
		html = html + "<meta property=\"og:title\" content=\"" + post.title + "\" />";
		html = html + "<meta property=\"og:description\" content=\"" + StringUtil.simpleSnippet(post.body) + "\" />";
		if (post.featuredImage != null)
			html = html + "<meta property=\"og:image\" content=\"" + amazonCloudService.getPublicImageURL(post.getImageLargeHash()) + "\" />";

		return html;
	}

	public String hiddenHtmlBuilder(Post post) throws IOException {
		String html = "";

		if (post.featuredImage != null)
			html = html + "<img class=\"hidden\" src=\"" + amazonCloudService.getPublicImageURL(post.getImageLargeHash()) + "\" />";
		html = html + "<h1 class=\"hidden\">" + post.title + "</h1>";
		html = html + "<div class=\"hidden\">" + post.body + "</div>";

		return html;
	}

	@Override
	public void destroy() {
	}


}
