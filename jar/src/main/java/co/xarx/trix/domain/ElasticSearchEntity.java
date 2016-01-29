package co.xarx.trix.domain;

import java.io.Serializable;

public interface ElasticSearchEntity extends Identifiable, MultiTenantEntity, Serializable {

	@Override
	Integer getId();

	String getType();
}
