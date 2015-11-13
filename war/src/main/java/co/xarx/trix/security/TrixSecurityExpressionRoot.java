package co.xarx.trix.security;


import co.xarx.trix.domain.*;
import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.List;

public class TrixSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

	private Object filterObject;
	private Object returnObject;
	private Object target;

	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository personStationRolesRepository;

	public TrixSecurityExpressionRoot(Authentication a) {
		super(a);
	}

	public boolean canWrite(Post post){
		boolean canWrite = false;

		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null && personLogged.id == post.author.id){
			Station station = post.station;
			if(station.visibility.equals(Station.UNRESTRICTED) && station.writable){
				canWrite = true;
			}else if(station.visibility.equals(Station.RESTRICTED_TO_NETWORKS) && station.writable){
				List<Integer> networksId = networkRepository.findIdsByStation(post.station.id);
				List<Network> belongsToNetworks = networkRepository.belongsToNetworks(personLogged.id, networksId);
				if(belongsToNetworks != null && belongsToNetworks.size() > 0){
					canWrite = true;
				}
			}else if(station.visibility.equals(Station.RESTRICTED) && station.writable){
				Station belongsToStation = stationRepository.belongsToStation(personLogged.id, post.station.id);
				if(belongsToStation != null){
					canWrite = true;
				}
			}else{
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(post.station, personLogged);
				if(personStationRoles != null && personStationRoles.writer){
					canWrite = true;
				}
			}
		}
		return canWrite;
	}

	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	public Object getFilterObject() {
		return filterObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	void setThis(Object target) {
		this.target = target;
	}

	public Object getThis() {
		return target;
	}

	@Override
	public boolean hasPermission(Object targetId, String targetType, Object permission) {
		try {
			return super.hasPermission(targetId, targetType, permission);
		} catch (AccessDeniedException e) {
			return false;
		}
	}

	@Override
	public boolean hasPermission(Object target, Object permission) {
		try {
			return super.hasPermission(target, permission);
		} catch (AccessDeniedException e) {
			return false;
		}
	}
}