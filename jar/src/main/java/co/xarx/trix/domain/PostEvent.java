package co.xarx.trix.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "z_post_events")
public class PostEvent extends Event {

	@Column(name = "person_id")
	public Integer personId;

	@Column(name = "post_state")
	public String postState;

	@Column(name = "post_id")
	public Integer postId;

	@Column(name = "session_id")
	public String sessionId;

	@Column(name = "device")
	public String device;

	@Column(name = "comment_id")
	public Integer commentId;

	@Column(name = "recommend_id")
	public Integer recommendId;

	public PostEvent(Post post, String typeEvent){
		this.postId = post.id;
		this.personId = post.author.id;
		this.stationId = post.stationId;
		this.typeEvent = typeEvent;
		this.postState = post.state;
	}

	public PostEvent(PostRead read){
		this.postId = read.post.id;
		this.personId = read.person != null ? read.person.id : null;
		this.stationId = read.post.stationId;
		this.typeEvent = Event.EVENT_READ;
		this.postState = read.post.state;
		this.sessionId = read.sessionid;
		this.device = UNKOWN_DEVICE;
	}

	public PostEvent(Comment comment, String typeEvent){
		this.postId = comment.post.id;
		this.personId = comment.author.id;
		this.stationId = comment.station.id;
		this.postState = comment.post.state;
		this.commentId = comment.id;
		this.device = UNKOWN_DEVICE;
		this.typeEvent = typeEvent;
	}

	public PostEvent(Recommend recommend, String typeEvent){
		this.recommendId = recommend.id;
		this.postId = recommend.post.id;
		this.personId = recommend.person.id;
		this.stationId = recommend.post.stationId;
		this.postState = recommend.post.state;
		this.typeEvent = typeEvent;
		this.device = UNKOWN_DEVICE;
	}

	public PostEvent() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}

	@Override
	public void build(Post post) {

	}
}
