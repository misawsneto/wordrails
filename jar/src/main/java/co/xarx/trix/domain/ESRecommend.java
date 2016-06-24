package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{properties.index}", type = Constants.ObjectType.RECOMMEND)
public class ESRecommend implements ElasticSearchEntity {

	public Integer id;

	public String tenantId;
	public String type;
	public Integer postId;
	public Date date;

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String getTenantId() {
		return null;
	}

	@Override
	public void setTenantId(String tenantId) {

	}
}
