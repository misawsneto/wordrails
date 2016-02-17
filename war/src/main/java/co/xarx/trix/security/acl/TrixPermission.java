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

	public static final Permission READ = new TrixPermission(1, 'R'); // 1
	public static final Permission WRITE = new TrixPermission(1 << 1, 'W'); // 2
	public static final Permission CREATE = new TrixPermission(1 << 2, 'C'); // 4
	public static final Permission DELETE = new TrixPermission(1 << 3, 'D'); // 8
	public static final Permission PUBLISH = new TrixPermission(1 << 4, 'P'); // 16
	public static final Permission SUPEREDITOR = new TrixPermission(1 << 5, 'E'); // 32
	public static final Permission ADMINISTRATION = new TrixPermission(1 << 6, 'A'); // 64
}
