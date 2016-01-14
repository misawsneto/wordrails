package co.xarx.trix.services;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.config.EventAuthorProvider;
import co.xarx.trix.domain.LogBuilder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.Term;
import co.xarx.trix.domain.event.PostEvent;
import co.xarx.trix.domain.event.StationEvent;
import co.xarx.trix.domain.event.TermEvent;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogBuilderExecutor implements LogBuilder {

	@Autowired
	private EventAuthorProvider authorProvider;

	@Autowired
	private WordrailsService wordrailsService;

	@Override
	public PostEvent build(String type, Post post) {
		PostEvent event = new PostEvent();
		event.setAuthor(authorProvider.getAuthor());
		event.setEventType(type);

		event.setPostId(post.getId());
		event.setPersonId(post.getAuthor().getId());
		event.setPostState(post.state);
		event.device = parseDevice((String) wordrailsService.session().getAttribute("userAgent"));

		return event;
	}

	@Override
	public StationEvent build(String type, Station station) {
		StationEvent event = new StationEvent();
		event.setAuthor(authorProvider.getAuthor());
		event.setEventType(type);

		event.setStationId(station.getId());
		event.setStationName(station.getName());
		event.device = parseDevice((String) wordrailsService.session().getAttribute("userAgent"));

		return event;
	}

	@Override
	public TermEvent build(String type, Term term) {
		TermEvent event = new TermEvent();
		event.setAuthor(authorProvider.getAuthor());
		event.setEventType(type);

		event.setTermId(term.getId());
		event.setParentId(term.getParent() != null ? term.getParent().getId() : null);
		event.setTermName(term.getName());
		event.device = parseDevice((String) wordrailsService.session().getAttribute("userAgent"));

		return event;
	}

	public String parseDevice(String userAgent){
		UserAgent ua = UserAgent.parseUserAgentString(userAgent);
		return ua.getBrowser().getName();
	}
}
