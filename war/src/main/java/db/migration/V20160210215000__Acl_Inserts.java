package db.migration;

import co.xarx.trix.config.flyway.SpringContextMigration;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.security.acl.TrixPermission;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
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
public class V20160210215000__Acl_Inserts extends SpringContextMigration {

	@Autowired
	private MutableAclService aclService;

	static StationRowMapper stationRowMapper = new StationRowMapper();
	static PersonRowMapper personRowMapper = new PersonRowMapper();
	static StationRoleRowMapper stationRoleRowMapper = new StationRoleRowMapper();

	@Override
	@SuppressWarnings("Duplicates")
	public void migrate() throws Exception {
		Map<Integer, MutableAcl> stationAcls = migrateStations();
		Map<Integer, MutableAcl> postsAcl = migratePosts(stationAcls);

		migrateComments(postsAcl);
	}

	public void migrateComments(Map<Integer, MutableAcl> postsAcl) {
		Map<Integer, List<Comment>> commentsMap = jdbc.query("SELECT * FROM comment " +
				"INNER JOIN person p ON comment.author_id = p.id", new CommentExtractor());

		for (Integer postId : commentsMap.keySet()) {
			List<Comment> comments = commentsMap.get(postId);

			for (Comment comment : comments) {
				TenantContextHolder.setCurrentTenantId(comment.getTenantId());
				Sid sid = new PrincipalSid(comment.getAuthor().getUsername());

				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(comment.getAuthor().getUsername(), "", new ArrayList() {
				}));

				ObjectIdentityImpl oi = new ObjectIdentityImpl(Comment.class, comment.id);
				MutableAcl acl = aclService.createAcl(oi);
				acl.setParent(postsAcl.get(postId));

				int permissionMask = TrixPermission.READ.getMask() | TrixPermission.UPDATE.getMask() | TrixPermission.DELETE.getMask();

				Permission permission = new TrixPermission(permissionMask);
				acl.insertAce(acl.getEntries().size(), permission, sid, true);
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
				Sid sid = new PrincipalSid(post.getAuthor().getUsername());

				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(post.getAuthor().getUsername(), "", new ArrayList() {
				}));

				ObjectIdentityImpl oi = new ObjectIdentityImpl(Post.class, post.id);
				MutableAcl acl = aclService.createAcl(oi);
				acl.setParent(stationAcls.get(stationId));

				int permissionMask = TrixPermission.READ.getMask() | TrixPermission.UPDATE.getMask() | TrixPermission.DELETE.getMask();

				Permission permission = new TrixPermission(permissionMask);
				acl.insertAce(acl.getEntries().size(), permission, sid, true);
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

		TransactionSynchronizationManager.initSynchronization();
		for (Integer stationId : stationRolesMap.keySet()) {
			Pair<List<StationRole>, String> stationRoles = stationRolesMap.get(stationId);
			TenantContextHolder.setCurrentTenantId(stationRoles.getLeft().get(0).getTenantId());

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(stationRoles.getRight(), "", new ArrayList() {
			}));

			ObjectIdentityImpl oi = new ObjectIdentityImpl(Station.class, stationId);
			MutableAcl acl = aclService.createAcl(oi);
			stationAcls.put(stationId, acl);
			for (StationRole stationRole : stationRoles.getLeft()) {
				Sid sid = new PrincipalSid(stationRole.person.username);

				int permissionMask = TrixPermission.READ.getMask();

				if (stationRole.editor) permissionMask |= TrixPermission.MODERATION.getMask() |
						TrixPermission.CREATE.getMask() |
						TrixPermission.UPDATE.getMask() |
						TrixPermission.DELETE.getMask();
				if (stationRole.writer || stationRole.station.writable)
					permissionMask |= TrixPermission.CREATE.getMask();
				if (stationRole.admin) permissionMask |= TrixPermission.ADMINISTRATION.getMask();

				if (stationRole.station.getVisibility().equals(Station.UNRESTRICTED)) {
					acl.insertAce(acl.getEntries().size(), TrixPermission.READ, new GrantedAuthoritySid("ROLE_ANONYMOUS"), true);
				}

				Permission permission = new TrixPermission(permissionMask);

				acl.insertAce(acl.getEntries().size(), permission, sid, true);
				acl.insertAce(acl.getEntries().size(), TrixPermission.ADMINISTRATION, new GrantedAuthoritySid("ROLE_ADMIN"), true);
			}
			aclService.updateAcl(acl);
		}

		return stationAcls;
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
}
