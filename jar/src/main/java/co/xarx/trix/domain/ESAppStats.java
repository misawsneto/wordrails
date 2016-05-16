package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "#{properties.index}", type = Constants.ObjectType.APP_STATS)
public class ESAppStats implements ElasticSearchEntity {

	public ESAppStats(){
		this.timestamp = System.currentTimeMillis()/1000;
	}

	@Id
	public Integer id;
	public String tenantId;

	@Field(type = FieldType.Float)
	public float averageRaiting;

	@Field(type = FieldType.Integer)
	public Integer downloads;

	@Field(type = FieldType.Integer)
	public Integer currentInstallations;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	public String packageName;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	public String sku;

	@Field(type = FieldType.Date)
	public long timestamp;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getType() {
		return Constants.ObjectType.APP_STATS;
	}

	@Override
	public String getTenantId() {
		return this.tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
