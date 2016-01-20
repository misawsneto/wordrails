package co.xarx.trix.config.multitenancy;

import co.xarx.trix.exception.TargetLookupFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

/**
 * A {@link CacheManager} implementation that supports multi-tenancy by wrapping around another
 * {@link CacheManager} implementation, the delegate, and decorating caches returned by the delegate
 * with {@link MultitenantCache}.
 *
 * @author Joe Laudadio (Joe.Laudadio@AltegraHealth.com)
 */
public final class MultitenantCacheManager implements CacheManager {

	private final CacheManager delegate;
	private final boolean contextRequired;

	/**
	 * Creates a new {@link MultitenantCacheManager} that wraps the given delegate. The contextRequired
	 * parameter defines whether or not calls to {@link #getCache(String)} should fail if there is no
	 * Multitentnat context defined by the {@link TenantContextHolder}.
	 *
	 * @param delegate
	 * @param contextRequired
	 */
	public MultitenantCacheManager(final CacheManager delegate, final boolean contextRequired) {
		if (delegate == null) {
			throw new NullPointerException("delegate may not be null");
		}
		this.delegate = delegate;
		this.contextRequired = contextRequired;
	}

	/**
	 * Convenience constructor equivalent to {@link MultitenantCacheManager(CacheManager, false)}
	 *
	 * @param delegate
	 * @throws TargetLookupFailureException
	 */
	public MultitenantCacheManager(final CacheManager delegate) throws TargetLookupFailureException {
		this(delegate, false);
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = this.delegate.getCache(name);
		String tenantContext = getTenantContext();
		if (this.contextRequired && tenantContext == null) {
			throw new TargetLookupFailureException("Tenant context required but not available");
		}
		if (cache != null) {
			logger.debug("Wrapped cache '{}' for tenant context '{}'", cache.getName(), tenantContext);
			cache = new MultitenantCache(cache, this.contextRequired);
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return this.delegate.getCacheNames();
	}

	public boolean isContextRequired() {
		return contextRequired;
	}

	private String getTenantContext() {
		String context = String.valueOf(TenantContextHolder.getCurrentNetworkId());
		// normalize empty string/whitespace as null context
		if (context != null && context.trim().isEmpty()) {
			context = null;
		}
		return context;
	}

	private final Logger logger = LoggerFactory.getLogger(MultitenantCacheManager.class);
}