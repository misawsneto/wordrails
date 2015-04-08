package com.wordrails.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PersonRepository;

@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class PersonsResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;
	
	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired PersonRepository personRepository;
	
	@PUT
	@Path("/me/regId")
	public void putRegId(@FormParam("regId") String regId) {
	}
		
	@PUT
	@Path("/me/password")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putPassword(@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) {
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		if(!username.equalsIgnoreCase("wordrails")) // don't allow users to change wordrails password
			userDetailsManager.changePassword(oldPassword, newPassword);
	}
	
	@GET
	@Path("/me")
	public void getCurrentPerson() {
		org.springframework.security.core.userdetails.User user = null;
		user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();		
		String path = httpServletRequest.getServletPath() + "/persons/search/findByUsername?username=" + username;
		httpRequest.forward(path);
	}
}