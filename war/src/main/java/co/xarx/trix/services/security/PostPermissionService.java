package co.xarx.trix.services.security;

import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostPermissionService {

	private StationRepository stationRepository;
	private PermissionEvaluator permissionEvaluator;

	@Autowired
	public PostPermissionService(StationRepository stationRepository, PermissionEvaluator permissionEvaluator) {
		this.stationRepository = stationRepository;
		this.permissionEvaluator = permissionEvaluator;
	}


	public boolean canPublishOnStation(Integer stationId) {
		boolean canWrite = permissionEvaluator.hasPermission(SecurityContextHolder.getContext()
				.getAuthentication(), stationId, Station
				.class.getName(), new Permission[]{Permissions.WRITE, Permissions.ADMINISTRATION, Permissions.MODERATION});

		if (!canWrite) {
			boolean hasPermissionToRead = permissionEvaluator.hasPermission(SecurityContextHolder.getContext()
					.getAuthentication(), stationId, Station
					.class.getName(), Permissions.READ);
			Station station = stationRepository.findOne(stationId);
			canWrite = station.isWritable() && hasPermissionToRead;
		}

		return canWrite;
	}


	public boolean canPublishPost(Integer postId) {
		return permissionEvaluator.hasPermission(SecurityContextHolder.getContext()
				.getAuthentication(), postId, Post
				.class.getName(), new Permission[]{Permissions.WRITE, Permissions.ADMINISTRATION, Permissions.MODERATION});
	}


	public boolean canCreateOnStation(Integer stationId) {
		return permissionEvaluator.hasPermission(SecurityContextHolder.getContext()
				.getAuthentication(), stationId, Station
				.class.getName(), Permissions.CREATE);
	}
}
