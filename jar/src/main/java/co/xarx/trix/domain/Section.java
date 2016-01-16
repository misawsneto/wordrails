package co.xarx.trix.domain;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class Section extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7424825842348684233L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	public String name;

	@Lob
	public String loggedInUrl;

	@Lob
	public String anonymousUrl;

	@Lob
	public String content;

	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;
}
