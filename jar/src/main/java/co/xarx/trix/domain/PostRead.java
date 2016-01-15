package co.xarx.trix.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"post_id", "person_session"})})
public class PostRead {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@ManyToOne
	public Person person;
	
	@ManyToOne
	public Post post;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date createdAt;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;
	
	public String sessionid;

	public String person_session;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		person_session = person != null ? person.id + "_" + sessionid : 0 + "_" + sessionid;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
	
}
