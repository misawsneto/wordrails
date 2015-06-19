package com.wordrails.business;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class is used by spring-security for authorizing users based on username and password.
 * For a functional representation of a user object for this system, check the {@link Person} class.  
 * @author misael
 */
@Entity
@Table(name="users")
public class User {
	@Id
	@Size(max=50)
	public String username;
	
	@NotNull
	@Size(max=500)	
	public String password;
	
	@NotNull
	public boolean enabled;

	@ElementCollection
	@CollectionTable(
		name="authorities", 
		joinColumns=@JoinColumn(name="username")
	)
	@Column(name="authority", length=50, nullable=true)
	public Set<String> authorities;
}