package co.xarx.trix.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.Serializable;


@Entity
public class Section extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7424825842348684233L;

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
