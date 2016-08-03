package co.xarx.trix.config.security;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.model.Permission;

public final class Permissions extends BasePermission {

	private static final long serialVersionUID = 6908982429414276318L;

	private Permissions(int mask, char code) {
		super(mask, code);
	}

	public static final Permission MODERATION = new Permissions(1 << 5, 'M'); // 32
	public static final Permission SPONSOR = new Permissions(1 << 6, 'S'); // 64

	public static boolean containsPermission(Permission cumulativePermission, Permission singlePermission) {
		return (cumulativePermission.getMask() & singlePermission.getMask()) == singlePermission.getMask();
	}

	public static CumulativePermission getOwner() {
		CumulativePermission permission = getReader();
		permission.set(WRITE);
		permission.set(DELETE);

		return permission;
	}

	public static CumulativePermission getAdmin() {
		CumulativePermission permission = getEditor();
		permission.set(ADMINISTRATION);

		return permission;
	}

	public static CumulativePermission getWriter() {
		CumulativePermission permission = getReader();
		permission.set(WRITE);
		permission.set(CREATE);

		return permission;
	}

	public static CumulativePermission getColaborator() {
		CumulativePermission permission = getReader();
		permission.set(CREATE);

		return permission;
	}

	public static CumulativePermission getSponsor() {
		CumulativePermission permission = getReader();
		permission.set(CREATE);
		permission.set(SPONSOR);

		return permission;
	}

	public static CumulativePermission getEditor() {
		CumulativePermission permission = getWriter();
		permission.set(MODERATION);
		permission.set(DELETE);

		return permission;
	}

	public static CumulativePermission getReader() {
		CumulativePermission permission = new CumulativePermission();
		permission.set(READ);

		return permission;
	}
}
