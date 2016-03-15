package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@SdkExclude
@Table(name = "userconnection")
public class UserConnection extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5655931808896715401L;

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Column(unique = true)
	public String accessToken;

	public String providerUserId;

	@JsonIgnore
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
