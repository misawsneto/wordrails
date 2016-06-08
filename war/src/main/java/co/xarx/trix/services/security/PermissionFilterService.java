package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IntegrationTestBean
@Component
public class PermissionFilterService {

	private PermissionEvaluator permissionEvaluator;

	@Autowired
	public PermissionFilterService(PermissionEvaluator permissionEvaluator) {
		this.permissionEvaluator = permissionEvaluator;
	}

	public List filterObjects(List items, String permission) {
		List result = new ArrayList();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		items.stream()
				.filter(item -> permissionEvaluator.hasPermission(authentication, item, permission))
				.forEach(result::add);

		return result;
	}

	public List<Integer> filterIds(List<Integer> items, Class clazz, String permission) {
		if(items == null)
			return null;

		List<Integer> result = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		items.stream()
				.filter(item -> permissionEvaluator.hasPermission(authentication, item, clazz.getName(), permission))
				.forEach(result::add);

		return result;
	}
}
