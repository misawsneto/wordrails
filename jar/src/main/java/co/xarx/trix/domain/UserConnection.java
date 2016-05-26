package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
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
	private String accessToken;

	private String providerUserId;

	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	private String providerId;

	private String displayName;

	private String profileUrl;

	private String email;

	private String imageUrl;

	@PreUpdate
	public void onUpdate() {
		setUpdatedAt(new Date());
	}

	@PrePersist
	public void onCreate() {
		setCreatedAt(new Date());
	}
}
