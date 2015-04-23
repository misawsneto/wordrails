package com.wordrails.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;

@Path("/terms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class TermResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;
	
	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired PersonRepository personRepository;
	
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired TaxonomyRepository taxonomyRepository;
	
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	
	@GET
	@Path("/termTree")
	public void putTermTree(@FormParam("regId") String regId) {
	} 
}