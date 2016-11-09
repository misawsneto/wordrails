package co.xarx.trix.web.filter;

import co.xarx.trix.api.PersonData;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.resource.v1.PostsResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

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

	@Autowired
	private PostService postService;

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
		String originalPath = (String) request.getAttribute("originalPath");
		boolean appliedPath = APPLIED_PATHS.contains(path) || APPLIED_PATHS.stream().anyMatch(p ->
				path != null && p != null &&
				!p.equals("") &&
				!path.equals("") &&
						path.startsWith(p));

		if (appliedPath) {

			if("/".equals(originalPath)){
				homeHiddenHtmlBuilder(request);
			}else if(originalPath.split("/").length == 3){
				String parts [] = originalPath.split("/");
				if("home".equals(parts[2])){
					String html = homeHiddenHtmlBuilder(request);
					request.setAttribute("requestedEntityJson", "null");
					request.setAttribute("requestedEntityMetas", "");
					request.setAttribute("requestedEntityHiddenHtml", html);
					request.setAttribute("entityType", "null");
				}else{
//					Post post = postRepository.findBySlug(parts[2]);
					PostView post = postService.getPostViewBySlug(parts[2], true);
					if (post != null) {
						request.setAttribute("requestedEntityJson", objectMapper.writeValueAsString(post));
						request.setAttribute("requestedEntityMetas", postMetaTagsBuilder(post));
						request.setAttribute("requestedEntityHiddenHtml", postHiddenHtmlBuilder(post));
						request.setAttribute("entityType", "POST");
					}

				}
			}else{
				if (originalPath.split("/").length == 2) {
					String parts[] = originalPath.split("/");
					Post post = postRepository.findBySlug(parts[1]);
					if (post != null) {
						HttpServletResponse httpResponse = (HttpServletResponse) res;
						httpResponse.sendRedirect("/" + post.station.stationSlug + "/" + post.slug);
						return;
					}
				}else {
					request.setAttribute("requestedEntityJson", "null");
					request.setAttribute("requestedEntityMetas", "");
					request.setAttribute("requestedEntityHiddenHtml", "");
					request.setAttribute("entityType", "");
				}
			}
		}

		chain.doFilter(req, res);
	}

	private void stationHiddenHtmlBuilder(HttpServletRequest request) {
	}

	private String homeHiddenHtmlBuilder(HttpServletRequest request) throws IOException {
		PersonData data = (PersonData) request.getAttribute("personDataObject");

		String template = FileUtil.loadFileFromResource("simple-home-template.html");

		StringWriter writer = new StringWriter();
		com.github.mustachejava.MustacheFactory mf = new com.github.mustachejava.DefaultMustacheFactory();
		com.github.mustachejava.Mustache mustache = mf.compile(new StringReader(template), "homeTemplate");

		mustache.execute(writer, data);
		writer.flush();
		return writer.toString();
	}

	public String postMetaTagsBuilder(PostView post) throws IOException {
		String html = "";

		html = html + "<meta property=\"og:url\" content=\"" + request.getRequestURL() + "\" />";
		html = html + "<meta property=\"og:title\" content=\"" + post.title + "\" />";
		html = html + "<meta property=\"og:description\" content=\"" + StringUtil.simpleSnippet(post.body) + "\" />";
		if (post.getImageLargeHash() != null)
			html = html + "<meta property=\"og:image\" content=\"" + amazonCloudService.getPublicImageURL(post.getImageLargeHash()) +
					"\" />";

		return html;
	}

	public String postHiddenHtmlBuilder(PostView post) throws IOException {
		HashMap<String, Object> scope = new HashMap<>();
		scope.put("post", post);
		scope.put("imageURL", amazonCloudService.getPublicImageURL(post.getImageLargeHash()));

		String template = FileUtil.loadFileFromResource("simple-post-template.html");

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
