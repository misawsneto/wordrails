package co.xarx.trix.domain;

import co.xarx.trix.domain.event.PostEvent;
import co.xarx.trix.domain.event.StationEvent;
import co.xarx.trix.domain.event.TermEvent;

public interface LogBuilder {

	PostEvent build(String type, Post post);

	StationEvent build(String type, Station station);

	TermEvent build(String type, Term term);

//	EventEntity build(String type, Person query);
}
