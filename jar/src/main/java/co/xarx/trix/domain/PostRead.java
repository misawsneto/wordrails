package co.xarx.trix.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "person_id", "sessionid"}))
public class PostRead extends BaseEntity {

	@ManyToOne
	public Person person;

	@ManyToOne
	public Post post;

	public String sessionid;
}
