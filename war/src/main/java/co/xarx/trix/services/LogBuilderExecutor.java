package co.xarx.trix.services;

import co.xarx.trix.config.EventAuthorProvider;
import co.xarx.trix.domain.LogBuilder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.Term;
import co.xarx.trix.domain.event.PostEvent;
import co.xarx.trix.domain.event.StationEvent;
import co.xarx.trix.domain.event.TermEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogBuilderExecutor implements LogBuilder {

	@Autowired
	private EventAuthorProvider authorProvider;

	@Override
	public PostEvent build(String type, Post post) {
		PostEvent event = new PostEvent();
		event.setAuthor(authorProvider.getAuthor());
		event.setEventType(type);

		event.setPostId(post.getId());
		event.setPersonId(post.getAuthor().getId());
		event.setPostState(post.state);
		event.setStationId(post.stationId);

		return event;
	}

	@Override
	public StationEvent build(String type, Station station) {
		StationEvent event = new StationEvent();
		event.setAuthor(authorProvider.getAuthor());
		event.setEventType(type);

		event.setStationId(station.getId());
		event.setStationName(station.getName());

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

		return event;
	}
}
