package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Set;

@Document(indexName = "#{properties.index}", type = Constants.ObjectType.PERSON)
public class ESPerson implements ElasticSearchEntity {

	@Id
	public Integer id;
	public String tenantId;
	public Integer networkId;

	public String name;
	public String username;

	public String email;
	public String twitter;
	public Set<Integer> bookmarkPosts;

	public String cover;
	public String profilePicture;

	@Id
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getType() {
		return Constants.ObjectType.PERSON;
	}

	public Set<Integer> getBookmarkPosts() {
		return bookmarkPosts;
	}

	public void setBookmarkPosts(Set<Integer> bookmarkPosts) {
		this.bookmarkPosts = bookmarkPosts;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public Integer getNetworkId() {
		return networkId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
}
