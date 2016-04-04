package co.xarx.trix.config.security;

import co.xarx.trix.domain.MultiTenantEntity;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.core.Authentication;

public class MultitenantPrincipalSid extends PrincipalSid implements MultiTenantEntity {

	private static final long serialVersionUID = -1010954518074773950L;

	private String tenantId;


	public MultitenantPrincipalSid(String principal, String tenantId) {
		super(principal);
		this.tenantId = tenantId;
	}

	public MultitenantPrincipalSid(Authentication authentication, String tenantId) {
		super(authentication);
		this.tenantId = tenantId;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(Object object) {
		if ((object == null) || !(object instanceof PrincipalSid)) {
			return false;
		}

		// Delegate to getPrincipal() to perform actual comparison (both should be
		// identical)
		return ((PrincipalSid) object).getPrincipal().equals(this.getPrincipal());
	}
}
