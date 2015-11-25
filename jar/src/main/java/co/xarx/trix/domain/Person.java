package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "person",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"user_id", "username"}),
				@UniqueConstraint(columnNames = {"username", "networkId"})
		})
public class Person extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7728358342573034233L;

	@Size(min = 1, max = 100)
	public String name;

	@Size(max = 50)
	@NotNull
	@Column(unique = true)
	@Pattern(regexp = "^[a-z0-9\\._-]{3,50}$")
	public String username;

	@OneToMany(mappedBy = "author")
	public Set<Comment> comments;

	@OneToMany(mappedBy = "person")
	public Set<StationRole> personsStationPermissions;

	@OneToMany(mappedBy = "person")
	public Set<NetworkRole> personsNetworkRoles;

	@OneToMany(mappedBy = "author")
	public Set<Post> posts;

	@OneToMany
	public Set<Person> following;

	@OneToMany(mappedBy = "person")
	public Set<Bookmark> bookmarks;

	@OneToMany(mappedBy = "person")
	public Set<Recommend> recommends;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	public User user;

	@Size(max = 2048)
	public String bio;

	@Column
	@Email
	public String email;

	@OneToOne
	public Image image;

	@OneToOne
	public Image cover;

	public String imageUrl;

	public String coverUrl;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastLogin;

	@ElementCollection()
	@JoinTable(name = "image_hash", joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "coverId"))
	@MapKeyColumn(name = "sizeTag", nullable = false)
	@Column(name = "hash", nullable = false)
	public Map<String, String> coverHashes;

	@ElementCollection()
	@JoinTable(name = "image_hash", joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "imageId"))
	@MapKeyColumn(name = "sizeTag", nullable = false)
	@Column(name = "hash", nullable = false)
	public Map<String, String> imageHashes;

	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;
	public Integer coverLargeId;
	public Integer coverId;
	public String imageHash;
	public String imageSmallHash;
	public String imageMediumHash;
	public String imageLargeHash;
	public String coverMediumHash;
	public String coverLargeHash;
	public String coverHash;
	@Transient
	public String password;
	@Transient
	public String passwordConfirm;
	public Boolean passwordReseted = false;
	public String twitterHandle;
	public Integer coverMediumId;

	@PrePersist
	public void onCreate() {
		onChange();
	}

	@PreUpdate
	public void onUpdate() {
		onChange();
	}

	@PostLoad //lazy initialize the maps. we need to do this hack because hibernate 4 has a bug to fetch eager null references
	void onPostLoad() {
		if(image != null) {
			this.imageHashes.size();
		}
		if(cover != null) {
			this.coverHashes.size();
		}
	}

	private void onChange() {
		if (image != null && image.originalHash != null) {
			imageHash = image.originalHash;
			imageSmallHash = image.smallHash;
			imageMediumHash = image.mediumHash;
			imageLargeHash = image.largeHash;

			imageId = image.original.id;
			imageSmallId = image.small.id;
			imageMediumId = image.medium.id;
			imageLargeId = image.large.id;
		} else {
			imageHash = null;
			imageSmallHash = null;
			imageMediumHash = null;
			imageLargeHash = null;

			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}

		if (cover != null && cover.originalHash != null) {
			coverHash = cover.originalHash;
			coverLargeHash = cover.largeHash;
			coverMediumHash = cover.mediumHash;

			coverId = cover.original.id;
			coverLargeId = cover.large.id;
			coverMediumId = cover.medium.id;
		} else {
			coverHash = null;
			coverLargeHash = null;
			coverMediumHash = null;

			coverId = null;
			coverLargeId = null;
			coverMediumId = null;
		}
	}
}
