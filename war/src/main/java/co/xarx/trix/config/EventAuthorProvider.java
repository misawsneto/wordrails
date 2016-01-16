package co.xarx.trix.config;

import co.xarx.trix.domain.event.Author;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

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
			author.sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		} catch (Exception e) {
			author.sessionId = "nosession";
		}
		return author;
	}
}
