package com.wordrails.domain;

import javax.persistence.*;

@Entity
public class Wordpress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	public String domain;
	public String username;
	public String password;
	public String token;

	@OneToOne
	public Station station;
}
