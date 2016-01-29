package co.xarx.trix.domain;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "person_id", "sessionid"}))
public class PostRead extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@ManyToOne
	public Person person;

	@ManyToOne
	public Post post;

	public String sessionid;
}
