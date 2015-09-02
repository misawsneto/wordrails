package com.wordrails.business;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class GlobalParameter {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Column(unique = true, name = "name")
	public String parameterName;

	@NotNull
	public String value;
}
