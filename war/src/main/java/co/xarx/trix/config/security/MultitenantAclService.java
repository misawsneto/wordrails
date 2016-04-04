package co.xarx.trix.config.security;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "SqlResolve"})
public class MultitenantAclService extends JdbcMutableAclService {

	private String insertSid = "insert into acl_sid (principal, sid, tenantId) values (?, ?, ?)";
	private String selectSidPrimaryKey = "select id from acl_sid where principal=? and sid=? and tenantId=?";


	public MultitenantAclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
		super(dataSource, lookupStrategy, aclCache);
	}

	@Override
	protected Long createOrRetrieveSidPrimaryKey(String sidName, boolean sidIsPrincipal, boolean allowCreate) {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		Assert.hasText(tenantId, "Tenant ID must not be null or empty");
		Long sidId = findSid(sidName, sidIsPrincipal, tenantId);

		if (sidId == null && allowCreate) {
			jdbcTemplate.update(insertSid, sidIsPrincipal, sidName, tenantId);
			Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), "Transaction must be running");

			sidId = findSid(sidName, sidIsPrincipal, tenantId);
		}

		return sidId;
	}

	private Long findSid(String sidName, boolean sidIsPrincipal, String tenantId) {
		List<Long> sidIds = jdbcTemplate.queryForList(selectSidPrimaryKey,
				new Object[]{Boolean.valueOf(sidIsPrincipal), sidName, tenantId}, Long.class);

		if (!sidIds.isEmpty()) {
			return sidIds.get(0);
		}
		return null;
	}
}
