package com.wordrails.business.query;

import javax.persistence.*;

@Entity
public class ElasticSearchQuery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Lob
	public String query;
}
