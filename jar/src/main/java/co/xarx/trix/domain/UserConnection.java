package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "userconnection")
public class UserConnection extends BaseEntity implements Serializable {

	@Column(unique = true)
	public String accessToken;

	public String providerUserId;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public User user;

	@NotNull
	public String providerId;

	public String displayName;

	public String profileUrl;

	public String email;

	public String imageUrl;
}
