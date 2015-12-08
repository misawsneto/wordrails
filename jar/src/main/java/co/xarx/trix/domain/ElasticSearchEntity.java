package co.xarx.trix.domain;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component("elasticSearchEntity")
public interface ElasticSearchEntity extends Identifiable, MultiTenantEntity {

	@Id
	@Override
	Integer getId();

	String getType();

	String getTenantId();
}
