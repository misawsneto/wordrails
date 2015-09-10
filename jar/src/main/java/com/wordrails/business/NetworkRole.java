package com.wordrails.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="person_network_role",uniqueConstraints = {@UniqueConstraint(columnNames={"person_id","network_id"})})
public class NetworkRole {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name="network_id")
	public Network network;
	
    
	@ManyToOne
	@NotNull
	@JoinColumn(name="person_id")
	public Person person;
	
	@NotNull
	public boolean admin;
}