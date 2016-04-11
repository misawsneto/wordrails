package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "person",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"user_id", "username"}),
				@UniqueConstraint(columnNames = {"username", "tenantId"})
		})
@JsonIgnoreProperties(value = {
		"imageHash", "imageLargeHash", "imageMediumHash", "imageSmallHash",
		"coverHash", "coverLargeHash", "coverMediumHash"
}, allowGetters = true, ignoreUnknown = true)
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
	@Pattern(regexp = "^[a-zA-Z0-9\\._-]{3,50}$")
	public String username;

	@OrderColumn(name = "list_order")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "person_bookmark",joinColumns = @JoinColumn(name = "person_id"))
	@Column(name = "post_id")
	public List<Integer> bookmarkPosts;

	@OrderColumn(name = "order")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "person_recommend",
			joinColumns = @JoinColumn(name = "person_id")

	)
	@Column(name = "post_id")
	public Set<Integer> recommendPosts;

	public boolean networkAdmin = false;

	@JsonIgnore
//	@RestResource(exported = false)
	@NotNull
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	public User user;

	@Size(max = 2048)
	public String bio;

	@Column
	@Email
	public String email;

//	@RestResource(exported = false)
	@ManyToOne
	public Image image;

//	@RestResource(exported = false)
	@ManyToOne
	public Image cover;

	@Transient
	@SdkInclude
	public String imageOriginalHash;
	@Transient
	@SdkInclude
	public String coverOriginalHash;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastLogin;

	@Transient
	@RestResource(exported = false)
	public String password;

	@Transient
	public String passwordConfirm;

	public String twitterHandle;

	@SdkInclude
	public String getImageHash() {
		if (image != null) return image.getOriginalHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getImageLargeHash() {
		if (image != null) return image.getLargeHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getImageMediumHash() {
		if (image != null) return image.getMediumHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getImageSmallHash() {
		if (image != null) return image.getSmallHash();

		return null;
	}

	@SdkInclude
	public String getCoverHash() {
		if (cover != null) return cover.getOriginalHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getCoverLargeHash() {
		if (cover != null) return cover.getLargeHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getCoverMediumHash() {
		if (cover != null) return cover.getMediumHash();

		return null;
	}

	@PostLoad
	public void postLoad(){
		if(image != null)
			imageOriginalHash = image.getOriginalHash();
		if(cover != null)
			coverOriginalHash = cover.getOriginalHash();
	}

}
