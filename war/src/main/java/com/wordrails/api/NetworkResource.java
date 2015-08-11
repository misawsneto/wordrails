package com.wordrails.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.business.BadRequestException;
import com.wordrails.persistence.*;
import com.wordrails.util.PersonCreateDto;
import com.wordrails.util.WordrailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.wordrails.util.NetworkCreateDto;
import org.springframework.validation.FieldError;

@Path("/networks")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource {

	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired
	TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired StationEventHandler stationEventHandler;
	private @Autowired PersonRepository personRepository;
	private @Autowired StationRoleEventHandler stationRoleEventHandler;
	private @Autowired TermRepository termRepository;

	public @Autowired @Qualifier("objectMapper")
	ObjectMapper mapper;

	@Path("/{id}/permissions")
	@GET
	public PersonPermissions getNetworkPersonPermissions(@PathParam("id") Integer id){
		PersonPermissions personPermissions = new PersonPermissions();
		Person person = authProvider.getLoggedPerson();

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

				stationPermissionDto.subheading = station.subheading;
				stationPermissionDto.sponsored = station.sponsored;
				stationPermissionDto.topper = station.topper;

				stationPermissionDto.allowComments = station.allowComments;
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

	@Path("/createNetwork")
	@POST
	public Response createNetwork (NetworkCreateDto networkCreate)  throws ConflictException, BadRequestException, JsonProcessingException {

		Network network = new Network();
		network.name = networkCreate.name;
		network.subdomain = networkCreate.subdomain;

		//Station Default Taxonomy
		Taxonomy nTaxonomy = new Taxonomy();
		nTaxonomy.name = "Categoria da Rede " + network.name;
		nTaxonomy.type = Taxonomy.NETWORK_TAXONOMY;

		taxonomyRepository.save(nTaxonomy);
		networkRepository.save(network);
		nTaxonomy.owningNetwork = network;
		taxonomyRepository.save(nTaxonomy);

		Term nterm1 = new Term();
		nterm1.name = "Categoria 1";

		Term nterm2 = new Term();
		nterm2.name = "Categoria 2";

		nterm1.taxonomy = nTaxonomy;
		nterm2.taxonomy = nTaxonomy;

		nTaxonomy.terms = new HashSet<Term>();
		nTaxonomy.terms.add(nterm1);
		nTaxonomy.terms.add(nterm2);
		termRepository.save(nterm1);
		termRepository.save(nterm2);
		taxonomyRepository.save(nTaxonomy);

		Person person = networkCreate.person;
		User user = null;

		Station station = new Station();
		station.name = network.name;
		station.main = true;
		station.networks = new HashSet<Network>();
		station.networks.add(network);
		station.visibility = Station.UNRESTRICTED;
		station.writable = false;

		Set<StationPerspective> perspectives = new HashSet<StationPerspective>(1);

		//Perspective Default
		StationPerspective stationPerspective = new StationPerspective();
		stationPerspective.station = station;
		stationPerspective.name = station.name + " (Default)";
		perspectives.add(stationPerspective);
		station.stationPerspectives = perspectives;

		Set<Taxonomy> taxonomies = new HashSet<Taxonomy>();

		//Station Default Taxonomy
		Taxonomy sTaxonomy = new Taxonomy();
		sTaxonomy.name = "Station: " + station.name;
		sTaxonomy.owningStation = station;
		sTaxonomy.type = Taxonomy.STATION_TAXONOMY;
		taxonomies.add(sTaxonomy);
		station.ownedTaxonomies = taxonomies;
		stationPerspective.taxonomy = sTaxonomy;

		//Tag Default Taxonomy
		Taxonomy tTaxonomy = new Taxonomy();
		tTaxonomy.name = "Tags " + station.name;
		tTaxonomy.owningStation = station;
		tTaxonomy.type = Taxonomy.STATION_TAG_TAXONOMY;
		taxonomies.add(tTaxonomy);
		station.ownedTaxonomies = taxonomies;

		stationRepository.save(station);

		try {
			UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
			authority.network = network;

			String password = person.password;

			user = new User();
			user.enabled = true;
			user.username = person.username;
			user.password = password;
			user.network = authority.network;
			authority.user = user;
			user.addAuthority(authority);

			person.user = user;

			personRepository.save(person);
		} catch (javax.validation.ConstraintViolationException e) {
			BadRequestException badRequest = new BadRequestException();

			for (ConstraintViolation violation : e.getConstraintViolations()) {
//					violation.get
				FieldError error = new FieldError(violation.getInvalidValue() + "", violation.getInvalidValue() + "", violation.getMessage());
				badRequest.errors.add(error);
			}

			throw badRequest;
		}

		NetworkRole networkRole = new NetworkRole();
		networkRole.network = network;
		networkRole.person = person;
		networkRole.admin = true;
		networkRolesRepository.save(networkRole);

		UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_NETWORK_ADMIN", network);
		user.addAuthority(authority);

		taxonomies = station.ownedTaxonomies;
		for (Taxonomy tax: taxonomies){
			if(tax.type.equals(Taxonomy.STATION_TAG_TAXONOMY)){
				if(station.tagsTaxonomyId == null)
					station.tagsTaxonomyId = tax.id;
			}
			if(tax.type.equals(Taxonomy.STATION_TAXONOMY)){
				if(station.categoriesTaxonomyId == null) {
					station.categoriesTaxonomyId = tax.id;
					// ---- create sample terms...
					Term term1 = new Term();
					term1.name = "Categoria 1";

					Term term2 = new Term();
					term2.name = "Categoria 2";

					term1.taxonomy = tax;
					term2.taxonomy = tax;

					tax.terms = new HashSet<Term>();
					tax.terms.add(term1);
					tax.terms.add(term2);
					termRepository.save(term1);
					termRepository.save(term2);
					taxonomyRepository.save(tax);
				}
			}
		}

		StationRole role = new StationRole();
		role.person = person;
		role.station = station;
		role.writer = true;
		role.admin = true;
		role.editor = true;
		stationRolesRepository.save(role);
		station.defaultPerspectiveId = new ArrayList<StationPerspective>(station.stationPerspectives).get(0).id;
		stationRepository.save(station);

		return Response.status(Status.CREATED).build();
	}

}