package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class MenuEntry extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7424825842348684233L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;

	@Lob
	private String loggedInUrl;

	@Lob
	private String anonymousUrl;

	@Lob
	private String content;
}
