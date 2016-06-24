package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{elasticsearch.nginxAccessIndex}",
		type = Constants.ObjectType.REQUEST_LOG)
public class AccessLog implements Identifiable, MultiTenantEntity, Serializable {

	public String id;

	public String type;

	public String request;

	public String tenantId;

	@Field(type = FieldType.Date)
	public Date timestamp;

	@Override
	public String getId() {
		return null;
	}

	public String getType() {
		return type;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
