package com.wordrails.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.GCMService;
import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class PersonsResource {
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
	private @Autowired PersonNetworkRegIdRepository pnrRepository;
	private @Autowired GCMService gcmService;
	
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	
	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putRegId(@FormParam("regId") String regId, @FormParam("networkId") Integer networkId) {
		Network network = networkRepository.findOne(networkId);
		Person person = accessControllerUtil.getLoggedPerson();
		gcmService.updateRegId(network, person, regId);
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
	
	@POST
	@Path("/create")
	public Response create(Person person){
		// TODO create user
		return Response.status(Status.CREATED).build();
	}
	
	@GET
	@Path("/init")
	public PersonData getInitialData (@Context HttpServletRequest request) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		
		Person person = accessControllerUtil.getLoggedPerson();
		
		if(person == null){
			throw new UnauthorizedException("User is not authorized");
		}
		
		Network network = wordrailsService.getNetworkFromHost(request);
		
		PersonPermissions personPermissions = new PersonPermissions();
		NetworkRole networkRole = networkRolesRepository.findByNetworkIdAndPersonId(network.id, person.id);
		List<Station> stations;
		List<StationDto> stationDtos = new ArrayList<StationDto>();
		
		if(networkRole != null){
			//Network Permissions
			NetworkPermission networkPermissionDto = new NetworkPermission();
			networkPermissionDto.networkId = networkRole.id;
			networkPermissionDto.admin = networkRole.admin;
			
			//Stations Permissions
			stations = stationRepository.findByPersonIdAndNetworkId(person.id, network.id);
			List<StationPermission> stationPermissionDtos = new ArrayList<StationPermission>(stations.size());
			for (Station station : stations) {
				StationPermission stationPermissionDto = new StationPermission();
				StationDto stationDto = new StationDto();
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
				
				//Station Fields
				stationPermissionDto.stationId = station.id;
				stationPermissionDto.stationName = station.name;
				stationPermissionDto.writable = station.writable;
				stationPermissionDto.main = station.main;
				stationPermissionDto.visibility = station.visibility;
				
				stationDto = mapper.readValue(mapper.writeValueAsString(station).getBytes(), StationDto.class);
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
				//StationRoles Fields
				StationRole stationRole = stationRolesRepository.findByStationAndPerson(station, person);
				if(stationRole != null){
					stationPermissionDto.admin = stationRole.admin;
					stationPermissionDto.editor = stationRole.editor;
					stationPermissionDto.writer = stationRole.writer;
				}
				
				stationPermissionDtos.add(stationPermissionDto);
				stationDtos.add(stationDto);
			}
			personPermissions.networkPermission = networkPermissionDto;
			personPermissions.stationPermissions = stationPermissionDtos;
			personPermissions.personId = person.id;
			personPermissions.username = person.username;
			personPermissions.personName = person.name;
			
		}
		
		PersonData initData = new PersonData();
		
		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes(), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes(), NetworkDto.class); 
		initData.networkRole = mapper.readValue(mapper.writeValueAsString(networkRole).getBytes(), NetworkRoleDto.class);
		initData.stations = stationDtos;
		initData.personPermissions = personPermissions;
		
		initData.person.links = generateSelfLinks(baseUrl + "/api/persons/" + person.id);
		initData.network.links = generateSelfLinks(baseUrl + "/api/stations/" + network.id);
		initData.networkRole.links = generateSelfLinks(baseUrl + "/api/networkRoles/" + networkRole.id);
		
		return initData;
	}
	
	private List<Link> generateSelfLinks(String self){
		Link link = new Link();
		link.href = self;
		link.rel = "self";
		return Arrays.asList(link);
	} 
}
