package com.wordrails.business;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class GlobalParameter {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Column(unique = true)
	public String name;

	@NotNull
	public String value;
}
