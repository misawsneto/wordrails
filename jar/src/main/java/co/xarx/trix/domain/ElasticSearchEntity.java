package co.xarx.trix.domain;

import org.springframework.stereotype.Component;

@Component("elasticSearchEntity")
public interface ElasticSearchEntity extends Identifiable {

	String getType();

	String getTenantId();
}
