package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event_log")
public class PostEventLog extends EventLog {

	public PostEventLog(Post post, String event){
		setEntityName("POST");
		setPostId(post.id);
		setAuthorId(post.author.id);
		setNetworkId(post.network.id);
		setEventName(event);
	}

	public PostEventLog() {
	}

	public Integer postId;

	public Integer authorId;

	public Integer networkId;

	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	public Date createdAt;

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public Integer getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
