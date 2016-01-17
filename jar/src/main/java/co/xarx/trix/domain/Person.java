package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"user_id", "username"}),
				@UniqueConstraint(columnNames = {"username", "networkId"})
		})
public class Person extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7728358342573034233L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@Size(min = 1, max = 100)
	public String name;

	@Size(max = 50)
	@NotNull
	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9\\._-]{3,50}$")
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

	@OrderColumn(name = "order")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "person_bookmark",
			joinColumns = @JoinColumn(name = "person_id")

	)
	@Column(name = "post_id")
	public Set<Integer> bookmarkPosts;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Integer> getBookmarkPosts() {
		if(bookmarkPosts == null) return new HashSet();
		return bookmarkPosts;
	}

	public void setBookmarkPosts(Set<Integer> bookmarkPosts) {
		this.bookmarkPosts = bookmarkPosts;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<StationRole> getPersonsStationPermissions() {
		return personsStationPermissions;
	}

	public void setPersonsStationPermissions(Set<StationRole> personsStationPermissions) {
		this.personsStationPermissions = personsStationPermissions;
	}

	public Set<NetworkRole> getPersonsNetworkRoles() {
		return personsNetworkRoles;
	}

	public void setPersonsNetworkRoles(Set<NetworkRole> personsNetworkRoles) {
		this.personsNetworkRoles = personsNetworkRoles;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<Person> getFollowing() {
		return following;
	}

	public void setFollowing(Set<Person> following) {
		this.following = following;
	}

	public Set<Recommend> getRecommends() {
		return recommends;
	}

	public void setRecommends(Set<Recommend> recommends) {
		this.recommends = recommends;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getCover() {
		return cover;
	}

	public void setCover(Image cover) {
		this.cover = cover;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Integer getImageSmallId() {
		return imageSmallId;
	}

	public void setImageSmallId(Integer imageSmallId) {
		this.imageSmallId = imageSmallId;
	}

	public Integer getImageMediumId() {
		return imageMediumId;
	}

	public void setImageMediumId(Integer imageMediumId) {
		this.imageMediumId = imageMediumId;
	}

	public Integer getImageLargeId() {
		return imageLargeId;
	}

	public void setImageLargeId(Integer imageLargeId) {
		this.imageLargeId = imageLargeId;
	}

	public Integer getCoverLargeId() {
		return coverLargeId;
	}

	public void setCoverLargeId(Integer coverLargeId) {
		this.coverLargeId = coverLargeId;
	}

	public Integer getCoverId() {
		return coverId;
	}

	public void setCoverId(Integer coverId) {
		this.coverId = coverId;
	}

	public String getImageHash() {
		return imageHash;
	}

	public void setImageHash(String imageHash) {
		this.imageHash = imageHash;
	}

	public String getImageSmallHash() {
		return imageSmallHash;
	}

	public void setImageSmallHash(String imageSmallHash) {
		this.imageSmallHash = imageSmallHash;
	}

	public String getImageMediumHash() {
		return imageMediumHash;
	}

	public void setImageMediumHash(String imageMediumHash) {
		this.imageMediumHash = imageMediumHash;
	}

	public String getImageLargeHash() {
		return imageLargeHash;
	}

	public void setImageLargeHash(String imageLargeHash) {
		this.imageLargeHash = imageLargeHash;
	}

	public String getCoverMediumHash() {
		return coverMediumHash;
	}

	public void setCoverMediumHash(String coverMediumHash) {
		this.coverMediumHash = coverMediumHash;
	}

	public String getCoverLargeHash() {
		return coverLargeHash;
	}

	public void setCoverLargeHash(String coverLargeHash) {
		this.coverLargeHash = coverLargeHash;
	}

	public String getCoverHash() {
		return coverHash;
	}

	public void setCoverHash(String coverHash) {
		this.coverHash = coverHash;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Boolean getPasswordReseted() {
		return passwordReseted;
	}

	public void setPasswordReseted(Boolean passwordReseted) {
		this.passwordReseted = passwordReseted;
	}

	public String getTwitterHandle() {
		return twitterHandle;
	}

	public void setTwitterHandle(String twitterHandle) {
		this.twitterHandle = twitterHandle;
	}

	public Integer getCoverMediumId() {
		return coverMediumId;
	}

	public void setCoverMediumId(Integer coverMediumId) {
		this.coverMediumId = coverMediumId;
	}

//	@Override
//	public EventEntity build(String type, LogBuilder builder) {
//		return null;
//	}
}
