package db.migration;

import co.xarx.trix.config.flyway.SpringContextMigration;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.persistence.StationRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class V20160202185000__Acl_Inserts extends SpringContextMigration {

	@Autowired
	private StationRolesRepository stationRolesRepository;

	@Override
	public void migrate() throws Exception {
//		this.jdbcTemplate = jdbc;
//
//		List<StationRole> stationRoles = jdbc.queryForList("select * from person_station_role", StationRole.class);
//		List<Person> people = jdbc.queryForList("select * from person", Person.class);
//
//		sids = jdbc.queryForList("select * from acl_sid", MultitenantPrincipalSid.class);
//		for (StationRole stationRole : stationRoles) {
//			ObjectIdentityImpl oi = new ObjectIdentityImpl(Station.class, stationRole.getId());
//			String username = null;
//			for (Person p : people) {
//				if(Objects.equals(p.getId(), stationRole.person.getId()))
//					username = p.username;
//			}
//			createAcl(oi);
//		}

		List<StationRole> stationRoles = stationRolesRepository.findAll();

		System.out.println("AHHHHHHHHHHHHH MLK ZIKA");
	}
}
