package com.wordrails.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.business.Taxonomy;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.util.NetworkCreateDto;

@Path("/networks")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource {
	
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired TaxonomyRepository taxonomyRepository;

	@Path("/{id}/permissions")
	@GET
	public PersonPermissions getNetworkPersonPermissions(@PathParam("id") Integer id){
		PersonPermissions personPermissions = new PersonPermissions();
		Person person = accessControllerUtil.getLoggedPerson();

		NetworkRole networkRole = networkRolesRepository.findByNetworkIdAndPersonId(id, person.id);
		if(networkRole != null){
			//Network Permissions
			NetworkPermission networkPermissionDto = new NetworkPermission();
			networkPermissionDto.networkId = networkRole.id;
			networkPermissionDto.admin = networkRole.admin;
			
			//Stations Permissions
			List<Station> stations = stationRepository.findByPersonIdAndNetworkId(person.id, id);
			List<StationPermission> stationPermissionDtos = new ArrayList<StationPermission>(stations.size());
			for (Station station : stations) {
				StationPermission stationPermissionDto = new StationPermission();
				
				//Station Fields
				stationPermissionDto.stationId = station.id;
				stationPermissionDto.stationName = station.name;
				stationPermissionDto.writable = station.writable;
				stationPermissionDto.main = station.main;
				stationPermissionDto.visibility = station.visibility;
				stationPermissionDto.defaultPerspectiveId = station.defaultPerspectiveId; 
				
				stationPermissionDto.social = station.social;
				stationPermissionDto.subheading = station.subheading;
				stationPermissionDto.sponsored = station.sponsored;
				stationPermissionDto.topper = station.topper;
				
				stationPermissionDto.allowComments = station.allowComments;
				stationPermissionDto.allowSignup = station.allowSignup;
				stationPermissionDto.allowSocialLogin = station.allowSocialLogin;
				stationPermissionDto.allowSocialShare = station.allowSocialShare;
				
				//StationRoles Fields
				StationRole stationRole = stationRolesRepository.findByStationAndPerson(station, person);
				if(stationRole != null){
					stationPermissionDto.admin = stationRole.admin;
					stationPermissionDto.editor = stationRole.editor;
					stationPermissionDto.writer = stationRole.writer;
				}
				
				stationPermissionDtos.add(stationPermissionDto);
			}
			personPermissions.networkPermission = networkPermissionDto;
			personPermissions.stationPermissions = stationPermissionDtos;
			personPermissions.personId = person.id;
			personPermissions.username = person.username;
			personPermissions.personName = person.name;
			
		}
		return personPermissions;
	}
	
	@Path("/create")
	@GET
	public Response createNetwork (NetworkCreateDto networkCreate){
		Person person = accessControllerUtil.getLoggedPerson();
		
		Network network = networkCreate.network;
		Taxonomy taxonomy = networkCreate.taxonomy;
		
		taxonomyRepository.save(taxonomy);
		
		networkRepository.save(network);
		
		NetworkRole networkRole = new NetworkRole();
		networkRole.admin = true;
		networkRole.person = person;
		networkRole.network = network;
		
		networkRolesRepository.save(networkRole);
		
		return Response.status(Status.CREATED).build();
	} 
	
}