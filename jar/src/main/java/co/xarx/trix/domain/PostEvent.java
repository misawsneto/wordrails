package co.xarx.trix.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "zevent_post")
@DiscriminatorValue(value="POST")
public class PostEvent extends Event {

	@Column(name = "person_id")
	public Integer personId;

	@Column(name = "post_state")
	public String postState;

	@Column(name = "post_id")
	public Integer postId;

	public PostEvent() {
	}

	public Integer getPersonId() {
		return personId;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getPostState() {
		return postState;
	}

	public void setPostState(String postState) {
		this.postState = postState;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
}
