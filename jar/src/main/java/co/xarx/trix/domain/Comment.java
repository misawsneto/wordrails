package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7637696909957859675L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}


	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date date;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastModificationDate;

	@Lob
	@NotNull
	public String body;

	@NotNull
	@ManyToOne
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