package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import org.springframework.stereotype.Service;

@Service
public class LogBuilderExecutor implements LogBuilder {

	@Override
	public PostEvent build(String type, Post post) {
		PostEvent pe = new PostEvent();
		pe.postId = post.id;
		pe.personId = post.author.id;
		pe.typeEvent = type;
		pe.postState = post.state;

		return pe;
	}

//	@Override
//	public EventEntity build(String type, Person person) {
//		return null;
//	}
}
