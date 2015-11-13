package com.wordrails.domain.query;

import javax.persistence.*;

@Entity
public class ElasticSearchQuery implements Query {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Lob
	public String query;

	@Override
	public String getQuery() {
		return query;
	}
}
