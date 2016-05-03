package co.xarx.trix.config.security;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public final class Permissions extends BasePermission {

	private static final long serialVersionUID = 6908982429414276318L;

	private Permissions(int mask, char code) {
		super(mask, code);
	}

	public static final Permission MODERATION = new Permissions(1 << 5, 'M'); // 32

	public static boolean containsPermission(Permission cumulativePermission, Permission singlePermission) {
		return (cumulativePermission.getMask() & singlePermission.getMask()) == singlePermission.getMask();
	}
}
