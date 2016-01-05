package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"post_id", "person_id"}))
public class Recommend extends BaseEntity {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "post_id")
	public Post post;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;
}
