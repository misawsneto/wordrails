package db.migration;

import co.xarx.trix.config.flyway.SpringContextMigration;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.security.acl.TrixPermission;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "unused"})
public class V20160210215000__Acl_Inserts extends SpringContextMigration {

	@Autowired
	private MutableAclService aclService;

	static Map<Integer, Pair<List<StationRole>, String>> stationRolesMap;

	static StationRowMapper stationRowMapper = new StationRowMapper();
	static PersonRowMapper personRowMapper = new PersonRowMapper();
	static StationRoleRowMapper stationRoleRowMapper = new StationRoleRowMapper();

	@Override
	public void migrate() throws Exception {
		stationRolesMap = jdbc.query("select * from person_station_role " +
				"inner join person p ON person_station_role.person_id = p.id", new StationRoleExtractor());

		TransactionSynchronizationManager.initSynchronization();
		for (Integer stationId : stationRolesMap.keySet()) {
			Pair<List<StationRole>, String> stationRoles = stationRolesMap.get(stationId);
			TenantContextHolder.setCurrentTenantId(stationRoles.getLeft().get(0).getTenantId());

			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(stationRoles.getRight(), "", new ArrayList(){}));

			ObjectIdentityImpl oi = new ObjectIdentityImpl(Station.class, stationId);
			MutableAcl acl = aclService.createAcl(oi);
			for (StationRole stationRole : stationRoles.getLeft()) {
				int permissionMask = TrixPermission.READ.getMask();

				if (stationRole.writer) permissionMask |= TrixPermission.PUBLISH.getMask();
				if (stationRole.editor) permissionMask |= TrixPermission.SUPEREDITOR.getMask();
				if (stationRole.admin) permissionMask |= TrixPermission.ADMINISTRATION.getMask();

				Permission permission = new TrixPermission(permissionMask);

				Sid sid = new PrincipalSid(stationRole.person.username);
				acl.insertAce(acl.getEntries().size(), permission, sid, true);
			}
			aclService.updateAcl(acl);
		}
	}

	private static class StationRoleRowMapper implements RowMapper<StationRole> {

		@Override
		public StationRole mapRow(ResultSet rs, int rowNum) throws SQLException {
			StationRole s = new StationRole();
			s.id = rs.getInt("id");
			s.writer = rs.getBoolean("writer");
			s.editor = rs.getBoolean("editor");
			s.admin = rs.getBoolean("admin");
			s.tenantId = rs.getString("tenantId");

			return s;
		}
	}

	private static class PersonRowMapper implements RowMapper<Person> {

		@Override
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person p = new Person();
			p.id = rs.getInt("p.id");
			p.username = rs.getString("p.username");
			p.networkAdmin = rs.getBoolean("p.networkAdmin");

			return p;
		}
	}

	private static class StationRowMapper implements RowMapper<Station> {

		@Override
		public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
			Station s = new Station();
			s.id = rs.getInt("station_id");

			return s;
		}
	}

	private static class StationRoleExtractor implements ResultSetExtractor<Map<Integer, Pair<List<StationRole>, String>>> {

		@Override
		public Map<Integer, Pair<List<StationRole>, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, Pair<List<StationRole>, String>> rolesMap = new HashMap<>();
			while (rs.next()) {
				Integer id = rs.getInt("station_id");
				List<StationRole> rolesList;
				Pair<List<StationRole>, String> roles = rolesMap.get(id);
				if (roles == null) {
					rolesList = new ArrayList<>();
					roles = new MutablePair<>(rolesList, null);
					rolesMap.put(id, roles);
				} else {
					rolesList = roles.getLeft();
				}

				StationRole role = stationRoleRowMapper.mapRow(rs, 0);
				role.person = personRowMapper.mapRow(rs, 0);
				role.station = stationRowMapper.mapRow(rs, 0);
				rolesList.add(role);

				if(role.person.networkAdmin) {
					roles.setValue(role.person.username);
					role.admin = true;
				}
			}
			return rolesMap;
		}
	}
}
