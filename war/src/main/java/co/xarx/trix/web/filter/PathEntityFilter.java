package co.xarx.trix.web.filter;

import co.xarx.trix.api.PersonData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

@Component("pathEntityFilter")
public class PathEntityFilter implements Filter {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private AmazonCloudService amazonCloudService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		String path = (String) request.getAttribute("originalPath");

		if (path != null && !path.startsWith("/css")
				&& !path.startsWith("/font")
				&& !path.startsWith("/fonts")
				&& !path.startsWith("/img")
				&& !path.startsWith("/js")
				&& !path.startsWith("/api")
				&& !path.startsWith("/l10n")
				&& !path.startsWith("/i18n")
				&& !path.startsWith("/libs")
				&& !path.startsWith("/views")
				&& !path.startsWith("/styles")
				&& !path.startsWith("/scripts")
				&& !path.startsWith("/images")
				&& !path.startsWith("/home")
				&& !path.startsWith("/404.html")
				&& !path.startsWith("/505.html")
				&& !path.startsWith("/access/createnetwork")) {

			if("/".equals(path)){
				homeHiddenHtmlBuilder(request);
			}else if(path.split("/").length == 3){
				String parts [] = path.split("/");
				if("home".equals(parts[2])){
					stationHiddenHtmlBuilder(request);
				}else{
					Post post = postRepository.findBySlug(parts[2]);
					if (post != null) {
						request.setAttribute("requestedEntityJson", objectMapper.writeValueAsString(post));
						request.setAttribute("requestedEntityMetas", postMetaTagsBuilder(post));
						request.setAttribute("requestedEntityHiddenHtml", postHiddenHtmlBuilder(post));
						request.setAttribute("entityType", "POST");
					}

				}
			}
		}

		chain.doFilter(req, res);
	}

	private void stationHiddenHtmlBuilder(HttpServletRequest request) {
	}

	private String homeHiddenHtmlBuilder(HttpServletRequest request) throws IOException {
		PersonData data = (PersonData) request.getAttribute("personDataObject");

		String template = FileUtil.loadTemplateHTML("simple-home-template.html");

		StringWriter writer = new StringWriter();
		com.github.mustachejava.MustacheFactory mf = new com.github.mustachejava.DefaultMustacheFactory();
		com.github.mustachejava.Mustache mustache = mf.compile(new StringReader(template), "homeTemplate");

		mustache.execute(writer, data);
		writer.flush();
		return writer.toString();
	}

	public String postMetaTagsBuilder(Post post) throws IOException {
		String html = "";

		html = html + "<meta property=\"og:url\" content=\"" + request.getRequestURL() + "\" />";
		html = html + "<meta property=\"og:title\" content=\"" + post.title + "\" />";
		html = html + "<meta property=\"og:description\" content=\"" + StringUtil.simpleSnippet(post.body) + "\" />";
		if (post.featuredImage != null)
			html = html + "<meta property=\"og:image\" content=\"" + amazonCloudService.getPublicImageURL(post
					.getImageHash()) +
					"\" />";

		return html;
	}

	public String postHiddenHtmlBuilder(Post post) throws IOException {
		HashMap<String, Object> scope = new HashMap<>();
		scope.put("post", post);
		scope.put("imageURL", amazonCloudService.getPublicImageURL(post.getImageLargeHash()));

		String template = FileUtil.loadTemplateHTML("simple-post-template.html");

		StringWriter writer = new StringWriter();
		com.github.mustachejava.MustacheFactory mf = new com.github.mustachejava.DefaultMustacheFactory();
		com.github.mustachejava.Mustache mustache = mf.compile(new StringReader(template), "homeTemplate");

		mustache.execute(writer, scope);
		writer.flush();
		return writer.toString();
	}

	@Override
	public void destroy() {
	}


}
