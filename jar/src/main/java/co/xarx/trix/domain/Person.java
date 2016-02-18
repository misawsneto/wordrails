package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "person",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"user_id", "username"}),
				@UniqueConstraint(columnNames = {"username", "tenantId"})
		})
public class Person extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7728358342573034233L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Size(min = 1, max = 100)
	public String name;

	@Size(max = 50)
	@NotNull
	@Pattern(regexp = "^[a-z0-9\\._-]{3,50}$")
	public String username;

	@OrderColumn(name = "order")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "person_bookmark",
			joinColumns = @JoinColumn(name = "person_id")

	)
	@Column(name = "post_id")
	public Set<Integer> bookmarkPosts;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	public User user;

	@Size(max = 2048)
	public String bio;

	@Column
	@Email
	public String email;

	@OneToOne
	@JsonIgnore
	public Image image;

	@OneToOne
	@JsonIgnore
	public Image cover;

	public String imageUrl;
	public String coverUrl;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastLogin;

	@Transient
	public String password;
	@Transient
	public String passwordConfirm;
	public Boolean passwordReseted = false;
	public String twitterHandle;

}
