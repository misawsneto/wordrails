package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Document(indexName = "trix", type = Constants.ObjectType.PERSON)
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
}
