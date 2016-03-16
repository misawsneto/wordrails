package co.xarx.trix.services;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

	private String profile;
	private TenantProvider tenantProvider;

	@Autowired
	public AccessService(@Value("${spring.profiles.active:'dev'}") String profile, TenantProvider tenantProvider) {
		this.profile = profile;
		this.tenantProvider = tenantProvider;
	}

	public boolean hasPermissionOnTenant(boolean isGranted, String[] tenants) {
		boolean inList = Sets.newHashSet(tenants).contains(tenantProvider.getTenantId());
		return isGranted == inList;
	}

	public boolean hasPermissionOnProfile(boolean isGranted, String... profiles) {
		boolean inList = Sets.newHashSet(profiles).contains(profile);
		return isGranted == inList;
	}
}
