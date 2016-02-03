package co.xarx.trix.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Notification extends BaseEntity {
	
	public enum Type{
		ADDED_TO_STATION, REMOVED_FROM_STATION, POST_COMMENTED, POST_DELETED, POST_ADDED, BREAKING_NEWS, MESSAGE, IREPORT_INVITE, IREPORT_REVOKE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	public String regId;

	@NotNull
	public String hash;
	
	@ManyToOne
	public Post post;

    public boolean test = true;

	public String status;

	public String errorCodeName;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=500)
	public String message;
	
	@NotNull
	@NotEmpty
	public String type;
}
