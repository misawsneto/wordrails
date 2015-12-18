package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "person_id"}))
public class Bookmark extends BaseEntity {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "post_id")
	public Post post;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
