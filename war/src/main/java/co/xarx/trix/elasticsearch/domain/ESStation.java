package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Document(indexName = "#{properties.index}", type = Constants.ObjectType.STATION)
public class ESStation implements ElasticSearchEntity {

	@Id
	public Integer id;
	public String tenantId;
	public Integer networkId;

	public String name;
	public Map<String, String> logo;

	@Id
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getType() {
		return Constants.ObjectType.STATION;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

	public Map<String, String> getLogo() {
		return logo;
	}

	public void setLogo(Map<String, String> logo) {
		this.logo = logo;
	}
}
