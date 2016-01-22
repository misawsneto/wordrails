package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "#{properties.index}", type = Constants.ObjectType.BOOKMARK)
public class ESBookmark implements ElasticSearchEntity {

	@Id
	public Integer id;
	public String tenantId;
	public Integer networkId;

	public Integer postId;
	public Integer personId;

	@Id
	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return Constants.ObjectType.BOOKMARK;
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

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
}
