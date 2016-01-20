package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date date;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastModificationDate;
	
	@Size(min=1, max=100)
	public String title;

	@Lob
	@NotNull
	public String body;

	@NotNull
	@ManyToOne
	@JsonBackReference
	@JoinColumn(updatable=false)
	public Person author;
	
	@NotNull
	@ManyToOne
	@JoinColumn(updatable=false)
	public Post post;
	
	@PrePersist
	public void onCreate() {
		if (date == null)
			date = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		lastModificationDate = new Date();
	}
}