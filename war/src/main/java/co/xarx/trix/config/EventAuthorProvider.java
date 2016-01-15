package co.xarx.trix.config;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.domain.event.Author;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import eu.bitwalker.useragentutils.UserAgent;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class EventAuthorProvider implements AuthorProvider {

	@Autowired
	private TrixAuthenticationProvider provider;

	@Autowired
	private WordrailsService wordrailsService;


	@Override
	public String provide() {
		Author author = new Author();

		author.userId = provider.getUser().getId();
		author.sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		return author.userId + ":" + author.sessionId;
	}


	public Author getAuthor() {
		Author author = new Author();

		author.userId = provider.getUser().getId();
		author.sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		author.device = parseDevice((String) wordrailsService.session().getAttribute("userAgent"));
		return author;
	}

	public String parseDevice(String userAgent){
		UserAgent ua = UserAgent.parseUserAgentString(userAgent);
		return ua.getBrowser().getName();
	}
}
