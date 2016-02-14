package co.xarx.trix.config;

import co.xarx.trix.domain.event.Author;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import eu.bitwalker.useragentutils.UserAgent;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Component
public class EventAuthorProvider implements AuthorProvider {

	@Autowired
	private TrixAuthenticationProvider provider;


	@Override
	public String provide() {
		Author author = getAuthor();

		return author.userId + ":" + author.sessionId;
	}


	public Author getAuthor() {
		Author author = new Author();

		author.userId = provider.getUser().getId();
		try {
			author.sessionId = session().getId();
			author.device = parseDevice((String) session().getAttribute("userAgent"));
		} catch (Exception e) {
			author.sessionId = "nosession";
			author.device = "nodevice";
		}

		return author;
	}

	private String parseDevice(String userAgent){
		UserAgent ua = UserAgent.parseUserAgentString(userAgent);
		return ua.getBrowser().getName();
	}

	private HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true); // true == allow create
	}
}
