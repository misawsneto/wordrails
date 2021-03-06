package db.migration;

import co.xarx.trix.config.flyway.SpringContextMigration;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.MultitenantPrincipalSid;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "unused"})
public class V10__Acl_Inserts extends SpringContextMigration {

	@Autowired
	private MutableAclService aclService;

	static StationRowMapper stationRowMapper = new StationRowMapper();
	static PersonRowMapper personRowMapper = new PersonRowMapper();
	static StationRoleRowMapper stationRoleRowMapper = new StationRoleRowMapper();

	@Override
	@SuppressWarnings("Duplicates")
	public void migrate() throws Exception {
		TransactionSynchronizationManager.initSynchronization();
		Map<Integer, MutableAcl> stationAcls = migrateStations();
		Map<Integer, MutableAcl> postsAcl = migratePosts(stationAcls);

		migrateComments(postsAcl);
		TransactionSynchronizationManager.clearSynchronization();
	}

	public void migrateComments(Map<Integer, MutableAcl> postsAcl) {
		Map<Integer, List<Comment>> commentsMap = jdbc.query("SELECT * FROM comment " +
				"INNER JOIN person p ON comment.author_id = p.id", new CommentExtractor());

		for (Integer postId : commentsMap.keySet()) {
			List<Comment> comments = commentsMap.get(postId);

			for (Comment comment : comments) {
				TenantContextHolder.setCurrentTenantId(comment.getTenantId());
				Sid sid = new MultitenantPrincipalSid(comment.getAuthor().getUsername(), comment.getTenantId());

				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(comment.getAuthor().getUsername(), "", new ArrayList() {
				}));

				ObjectIdentityImpl oi = new ObjectIdentityImpl(Comment.class, comment.id);
				MutableAcl acl = aclService.createAcl(oi);
				acl.setParent(postsAcl.get(postId));

				acl.insertAce(acl.getEntries().size(), getPermissionRWD(), sid, true);
				aclService.updateAcl(acl);
			}
		}
	}

	public Map<Integer, MutableAcl> migratePosts(Map<Integer, MutableAcl> stationAcls) {
		Map<Integer, MutableAcl> postsAcl = new HashMap<>();
		Map<Integer, List<Post>> postsMap = jdbc.query("SELECT * FROM post " +
				"INNER JOIN person p ON post.author_id = p.id", new PostExtractor());

		for (Integer stationId : postsMap.keySet()) {
			List<Post> posts = postsMap.get(stationId);

			for (Post post : posts) {
				TenantContextHolder.setCurrentTenantId(post.getTenantId());
				Sid sid = new MultitenantPrincipalSid(post.getAuthor().getUsername(), post.getTenantId());

				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(post.getAuthor().getUsername(), "", new ArrayList() {
				}));

				ObjectIdentityImpl oi = new ObjectIdentityImpl(Post.class, post.id);
				MutableAcl acl = aclService.createAcl(oi);
				acl.setParent(stationAcls.get(stationId));

				acl.insertAce(acl.getEntries().size(), getPermissionRWD(), sid, true);
				aclService.updateAcl(acl);
				postsAcl.put(post.getId(), acl);
			}
		}
		return postsAcl;
	}

	public Map<Integer, MutableAcl> migrateStations() {
		Map<Integer, MutableAcl> stationAcls = new HashMap<>();
		Map<Integer, Pair<List<StationRole>, String>> stationRolesMap = jdbc.query("SELECT * FROM person_station_role " +
				"INNER JOIN person p ON person_station_role.person_id = p.id " +
				"INNER JOIN station s ON person_station_role.station_id = s.id", new StationRoleExtractor());

		for (Integer stationId : stationRolesMap.keySet()) {
			Pair<List<StationRole>, String> stationRoles = stationRolesMap.get(stationId);
			TenantContextHolder.setCurrentTenantId(stationRoles.getLeft().get(0).getTenantId());

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(stationRoles.getRight(), "", new ArrayList() {
			}));

			ObjectIdentityImpl oi = new ObjectIdentityImpl(Station.class, stationId);
			MutableAcl acl = aclService.createAcl(oi);
			stationAcls.put(stationId, acl);
			for (StationRole stationRole : stationRoles.getLeft()) {
				Sid sid = new MultitenantPrincipalSid(stationRole.person.username, stationRole.getTenantId());

				CumulativePermission permission = new CumulativePermission();
				permission.set(Permissions.READ);

				if (stationRole.editor) {
					permission.set(Permissions.MODERATION);
					permission.set(Permissions.CREATE);
					permission.set(Permissions.WRITE);
					permission.set(Permissions.DELETE);
				}
				if (stationRole.writer) {
					permission.set(Permissions.WRITE);
					permission.set(Permissions.CREATE);
				}
				if (stationRole.admin) {
					permission = getAdmPermission();
				}

				boolean anonymousCanRead = stationRole.station.getVisibility().equals(Station.UNRESTRICTED);
				acl.insertAce(acl.getEntries().size(), Permissions.READ,
						new GrantedAuthoritySid("ROLE_USER"), anonymousCanRead);
				acl.insertAce(acl.getEntries().size(), Permissions.READ,
						new GrantedAuthoritySid("ROLE_ANONYMOUS"), anonymousCanRead);
				acl.insertAce(acl.getEntries().size(), permission, sid, true);
				acl.insertAce(acl.getEntries().size(), getAdmPermission(),
						new GrantedAuthoritySid("ROLE_ADMIN"), true);
			}
			aclService.updateAcl(acl);
		}

		return stationAcls;
	}

	@SuppressWarnings("Duplicates")
	private CumulativePermission getAdmPermission() {
		CumulativePermission permission = new CumulativePermission();
		permission.set(Permissions.READ);
		permission.set(Permissions.MODERATION);
		permission.set(Permissions.CREATE);
		permission.set(Permissions.WRITE);
		permission.set(Permissions.DELETE);
		permission.set(Permissions.ADMINISTRATION);

		return permission;
	}

	@SuppressWarnings("Duplicates")
	private Permission getPermissionRWD() {
		CumulativePermission permission = new CumulativePermission();
		permission.set(Permissions.READ);
		permission.set(Permissions.WRITE);
		permission.set(Permissions.DELETE);

		return permission;
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
			s.visibility = rs.getString("visibility");
			s.writable = rs.getBoolean("writable");

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

				if (role.person.networkAdmin) {
					roles.setValue(role.person.username);
				}
			}

			return rolesMap;
		}
	}

	private static class PostExtractor implements ResultSetExtractor<Map<Integer, List<Post>>> {

		@Override
		public Map<Integer, List<Post>> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, List<Post>> postsMap = new HashMap<>();

			while (rs.next()) {
				Integer stationId = rs.getInt("station_id");
				List<Post> posts = postsMap.get(stationId);
				if (posts == null) {
					posts = new ArrayList<>();
					postsMap.put(stationId, posts);
				}

				Post p = new Post();
				p.id = rs.getInt("id");
				p.author = personRowMapper.mapRow(rs, 0);
				p.tenantId = rs.getString("tenantId");
				posts.add(p);
			}

			return postsMap;
		}
	}

	private static class CommentExtractor implements ResultSetExtractor<Map<Integer, List<Comment>>> {

		@Override
		public Map<Integer, List<Comment>> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, List<Comment>> commentsMap = new HashMap<>();

			while (rs.next()) {
				Integer postId = rs.getInt("post_id");
				List<Comment> comments = commentsMap.get(postId);
				if (comments == null) {
					comments = new ArrayList<>();
					commentsMap.put(postId, comments);
				}

				Comment c = new Comment();
				c.id = rs.getInt("id");
				c.author = personRowMapper.mapRow(rs, 0);
				c.tenantId = rs.getString("tenantId");
				comments.add(c);
			}

			return commentsMap;
		}
	}

	@Setter
	@Getter
	static class StationRole extends BaseEntity {

		public Integer id;
		public Station station;
		public Person person;
		public boolean editor;
		public boolean writer;
		public boolean admin;

		public Integer getStationId() {
			if (station != null)
				return station.id;
			return null;
		}
	}
}
