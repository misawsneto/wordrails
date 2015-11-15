package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;

import javax.persistence.*;

@Entity
public class ElasticSearchQuery extends BaseEntity implements Query {

	@Lob
	public String query;

	@Override
	public String getQuery() {
		return query;
	}
}
