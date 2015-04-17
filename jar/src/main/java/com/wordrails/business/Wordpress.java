package com.wordrails.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Wordpress {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
    
    public String domain;    
    public String username;    
    public String password;    
    public String token;    

	@OneToOne
	public Station station;
}
