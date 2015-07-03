package com.wordrails.business;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class is used by spring-security for authorizing users based on username and password.
 * For a functional representation of a user object for this system, check the {@link Person} class.  
 * @author misael
 */
@Entity
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames={"networkId", "username"}))
public class User {
	@Id
	@Size(max=50)
	public String username;
	
	@NotNull
	@Size(max=500)	
	public String password;
	
	@NotNull
	public boolean enabled;

	@NotNull
	public Integer networkId;

	@ElementCollection
	@CollectionTable(
		name="authorities", 
		joinColumns=@JoinColumn(name="username")
	)
	@Column(name="authority", length=50, nullable=true)
	public Set<String> authorities;
}