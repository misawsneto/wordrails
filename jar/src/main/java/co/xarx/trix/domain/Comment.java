package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
public class Comment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7637696909957859675L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public int commentsCount;

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

	@SdkInclude
	@NotNull
	@ManyToOne
	@JoinColumn(updatable=false)
	public Person author;

	@SdkInclude
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