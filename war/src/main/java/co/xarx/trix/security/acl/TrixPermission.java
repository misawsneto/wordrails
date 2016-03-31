package co.xarx.trix.security.acl;

import org.springframework.security.acls.domain.AbstractPermission;
import org.springframework.security.acls.model.Permission;

public class TrixPermission extends AbstractPermission {

	private static final long serialVersionUID = 6908982429414276318L;

	public TrixPermission(int mask) {
		super(mask);
	}

	public TrixPermission(int mask, char code) {
		super(mask, code);
	}

	public static final Permission ADMINISTRATION = new TrixPermission(1, 'A'); // 1

	public static final Permission READ = new TrixPermission(1 << 1, 'R'); // 2
	public static final Permission UPDATE = new TrixPermission(1 << 2, 'U'); // 4
	public static final Permission CREATE = new TrixPermission(1 << 3, 'C'); // 8
	public static final Permission DELETE = new TrixPermission(1 << 4, 'D'); // 16
	public static final Permission MODERATION = new TrixPermission(1 << 5, 'M'); // 32
}
