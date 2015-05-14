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
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"person_id", "network_id", "regId"}))
public class PersonNetworkRegId {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;

	public String regId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;
}
