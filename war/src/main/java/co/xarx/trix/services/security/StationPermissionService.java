package co.xarx.trix.services.security;

import co.xarx.trix.api.StationPermission;
import co.xarx.trix.api.StationRolesUpdate;
import co.xarx.trix.api.v2.PermissionData;
import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.api.v2.StationPermissionData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static co.xarx.trix.config.security.Permissions.*;
import static org.springframework.security.acls.domain.BasePermission.READ;

@Service
public class StationPermissionService {

	private AccessControlListService aclService;
	private StationRepository stationRepository;
	private PermissionEvaluator permissionEvaluator;
	private PersonRepository personRepository;
	private ModelMapper mapper;

	@Autowired
	public StationPermissionService(AccessControlListService aclService,
									StationRepository stationRepository,
									PermissionEvaluator permissionEvaluator,
									PersonRepository personRepository,
									ModelMapper mapper) {
		this.aclService = aclService;
		this.stationRepository = stationRepository;
		this.permissionEvaluator = permissionEvaluator;
		this.personRepository = personRepository;
		this.mapper = mapper;
	}

	public void updateStationsPermissions(StationRolesUpdate dto, String loggedUsername){
		dto.usernames.remove(loggedUsername);

		List<Sid> sids = dto.usernames.stream().map(PrincipalSid::new).collect(Collectors.toList());
		updateStationsPermissions(sids, dto.stationsIds, dto.colaborate, dto.writer, dto.editor, dto.admin);
	}

	public List<Integer> findStationsWithReadPermission() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Integer> stationIds = stationRepository.findIds(TenantContextHolder.getCurrentTenantId());

		return getIdsOfStationsWithReadPermissions(auth, stationIds);
	}

	private List<Integer> getIdsOfStationsWithReadPermissions(Authentication auth, List<Integer> stationIds) {
		return stationIds.stream()
				.filter(stationId ->
						permissionEvaluator.hasPermission(auth, stationId, Station.class.getName(), READ)
				)
				.collect(Collectors.toList());
	}

	public void updateStationsPermissions(List<Sid> sids, List<Integer> stationIds, boolean colaborate, boolean writer, boolean
			editor, boolean admin) {
		Assert.notEmpty(sids, "Sids must have elements");
		Assert.notEmpty(stationIds, "Station ids must have elements");


		CumulativePermission permission = Permissions.getReader();

		if(colaborate){
			permission = Permissions.getColaborator();
		}
		if (editor) {
			permission = Permissions.getEditor();
		}
		if (writer) {
			permission = Permissions.getWriter();
		}
		if (admin) {
			permission = Permissions.getAdmin();
		}

		Map<ObjectIdentity, MutableAcl> acls = aclService.findAcls(stationIds);
		for (MutableAcl acl : acls.values()) {
			List<AccessControlEntry> entries = acl.getEntries();
			for (Sid sid : sids) {
				List<AccessControlEntry> aces = aclService.findAce(entries, sid);
				if(aces == null) {
					acl.insertAce(acl.getEntries().size(), permission, sid, true);
				} else {
					for (AccessControlEntry ace : aces) {
						acl.updateAce(entries.indexOf(ace), permission);
					}
				}

				aclService.updateAcl(acl);
			}
		}
	}

	public StationPermissionData getPermissions(Integer stationId) {
		MutableAcl acl = aclService.findAcl(stationId);

		StationPermissionData result = new StationPermissionData();
		result.setStationId(stationId);
		for (AccessControlEntry ace : acl.getEntries()) {
			boolean isNotUserPermission = !(ace.getSid() instanceof PrincipalSid);
			if(isNotUserPermission) {
				continue;
			}
			String username = ((PrincipalSid) ace.getSid()).getPrincipal();

			Permission p = ace.getPermission();
			PermissionData permissionData = aclService.getPermissionData(p);

			StationPermissionData.Permission entry = result.new Permission(username, permissionData, null);

			result.getUserPermissions().add(entry);
		}

		return result;
	}

	public void deleteStationPermissions(List<String> usernames, List<Integer> stationIds) {
		Map<ObjectIdentity, MutableAcl> acls = aclService.findAcls(stationIds);
		for (MutableAcl acl : acls.values()) {
			for (int i = 0; i < acl.getEntries().size(); i++) {
				AccessControlEntry ace = acl.getEntries().get(i);
				if (usernames.contains(ace.getSid().toString())) {
					acl.deleteAce(i);
				}
			}
		}
	}

	public List<StationPermission> getStationPermissions(List<Station> stations) {
		List<StationPermission> stationPermissionDtos = new ArrayList<>();
		for (Station station : stations) {
			StationPermission stationPermissionDto = new StationPermission();

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
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				stationPermissionDto.admin = permissionEvaluator.hasPermission(auth, station, ADMINISTRATION);
				stationPermissionDto.editor = permissionEvaluator.hasPermission(auth, station, MODERATION);
				stationPermissionDto.writer = permissionEvaluator.hasPermission(auth, station, CREATE);
			}

			stationPermissionDtos.add(stationPermissionDto);
		}

		return stationPermissionDtos;
	}

	public void getPersons(StationPermissionData data) {
		List<StationPermissionData.Permission> permissions = data.getUserPermissions();
		if(permissions != null && permissions.size() > 0){
			List<String> usernames = permissions.stream().map(StationPermissionData.Permission::getUsername).collect(Collectors.toList());
			List<Person> persons = personRepository.findByUsernameIn(usernames);
			for (StationPermissionData.Permission p : permissions) {
				for (Person person: persons) {
					if(p.getUsername().equals(person.username)){
						p.setPerson(mapper.map(person, PersonData.class));
					}
				}
			}
		}
	}
}
