package co.xarx.trix.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "event")
public class PostEvent extends Event {

	@Transient
	public Post eventPost;

	public PostEvent(Post post, String typeEvent){
		setTypeEvent(typeEvent);
		setEventPost(post);
	}

	public PostEvent() {
	}

	public Post getEventPost() {
		return eventPost;
	}

	public void setEventPost(Post eventPost) {
		this.eventPost = eventPost;
	}

	@Override
	public Event build() {
		return this;
	}
}
