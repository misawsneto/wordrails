package co.xarx.trix.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
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
