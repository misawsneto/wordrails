package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "person_id"}))
public class Bookmark {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "post_id")
	public Post post;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
}
