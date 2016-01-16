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
				@UniqueConstraint(columnNames = {"username", "tenantId"})
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

	@Transient
	public String password;
	@Transient
	public String passwordConfirm;
	public Boolean passwordReseted = false;
	public String twitterHandle;

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
}
