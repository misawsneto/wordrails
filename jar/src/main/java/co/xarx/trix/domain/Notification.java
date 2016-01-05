package co.xarx.trix.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Notification extends BaseEntity {
	
	public enum Type{
		ADDED_TO_STATION, REMOVED_FROM_STATION, POST_COMMENTED, POST_DELETED, POST_ADDED, BREAKING_NEWS, MESSAGE, IREPORT_INVITE, IREPORT_REVOKE
	}
	
	@ManyToOne
	@NotNull
	public Person person;
	
	@ManyToOne
	@NotNull
	public Network network;
	
	@NotNull
	public String hash;
	
	@ManyToOne
	public Station station;
	
	@ManyToOne
	public Post post;
	
	public Integer postId;
	
	public boolean seen = false;

    public boolean test = true;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=500)
	public String message;
	
	@NotNull
	@NotEmpty
	public String type;

	@PrePersist
	void onCreate() {
		if(post!=null){
			postId = post.id;
		}
	}
}
