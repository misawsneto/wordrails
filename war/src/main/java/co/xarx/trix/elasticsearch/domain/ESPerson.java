package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Document(indexName = "${elasticsearch.index}", type = Constants.ObjectType.PERSON)
public class ESPerson implements ElasticSearchEntity {

	@Id
	public Integer id;
	public String tenantId;
	public Integer networkId;

	public String name;
	public String username;

	public String email;
	public String twitter;
	public Map<String, String> cover;
	public Map<String, String> profilePicture;

	@Id
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getType() {
		return Constants.ObjectType.PERSON;
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

	public Map<String, String> getCover() {
		return cover;
	}

	public void setCover(Map<String, String> cover) {
		this.cover = cover;
	}

	public Map<String, String> getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(Map<String, String> profilePicture) {
		this.profilePicture = profilePicture;
	}
}
