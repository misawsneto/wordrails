package co.xarx.trix.config.multitenancy;

import co.xarx.trix.exception.TargetLookupFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link Cache} implementation that provides support for multi-tenancy by translating the lookup
 * keys into a tenant-context-specific key using {@link TenantContextHolder}.  The nuts and bolts of the
 * caching are the responsibility of the delegate {@link Cache} implementation that this class
 * wraps.  This class only deals with translating the keys for the tenant context.
 * <p>
 * Whether or not null values are allowed is determined by the underlying {@link Cache} implementation.
 * The {@link MultitenantCache} does not have a problem with null values itself.
 * <p>
 * Care should be taken if using {@link #getNativeCache()} because the keys contained in that cache
 * instance will not match the keys that were given (they will have been translated into something
 * tenant-specific).
 *
 * @author Joe Laudadio (Joe.Laudadio@AltegraHealth.com)
 */
public final class MultitenantCache implements Cache {

	public final Cache delegate;
	public final boolean contextRequired;

	/**
	 * Creates a new {@link MultitenantCache} that wraps the given delegate. The contextRequired
	 * parameter defines whether or not cache methods should fail if there is no
	 * Multitentnat context defined by the {@link TenantContextHolder}.
	 *
	 * @param delegate
	 * @param contextRequired
	 */
	public MultitenantCache(final Cache delegate, final boolean contextRequired) {
		Assert.notNull(delegate, "delegate may not be null");

		this.delegate = delegate;
		this.contextRequired = contextRequired;
	}

	/**
	 * Convenience constructor for {@link MultitenantCache(Cache, false)}
	 *
	 * @param delegate
	 */
	public MultitenantCache(final Cache delegate) {
		this(delegate, false);
	}

	@Override
	public String getName() {
		return this.delegate.getName();
	}

	@Override
	public Object getNativeCache() {
		return this.delegate.getNativeCache();
	}

	@Override
	public ValueWrapper get(Object key) {
		Object translatedKey = translateKey(key);
		return this.delegate.get(translatedKey);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		Object translatedKey = translateKey(key);
		return delegate.get(translatedKey, type);
	}

	@Override
	public void put(Object key, Object value) {
		Object translatedKey = translateKey(key);
		this.delegate.put(translatedKey, value);
		;
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object translatedKey = translateKey(key);
		return delegate.putIfAbsent(translatedKey, value);
	}

	@Override
	public void evict(Object key) {
		Object translatedKey = translateKey(key);
		this.delegate.evict(translatedKey);
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	public boolean isContextRequired() {
		return this.contextRequired;
	}

	private TenantKey translateKey(Object key) throws TargetLookupFailureException {
		logger.debug("Translating key {}", key);
		String tenantContext = String.valueOf(TenantContextHolder.getCurrentNetworkId());
		if (this.contextRequired && (tenantContext == null || tenantContext.trim().isEmpty())) {
			throw new TargetLookupFailureException("Tenant context is required but is not available");
		}
		TenantKey translatedKey = new TenantKey(tenantContext, key, this.getName());
		logger.debug("Translated key: {}", translatedKey);
		return translatedKey;
	}

	private final Logger logger = LoggerFactory.getLogger(MultitenantCache.class);

	static class TenantKey implements Serializable {

		private final String tenantContext;
		private final Object key;
		private final String cacheName;

		/**
		 * null values are ok
		 */
		public TenantKey(final String tenantContext, Object key, String cacheName) {
			this.tenantContext = tenantContext;
			this.key = key;
			this.cacheName = cacheName;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof TenantKey)) {
				return false;
			}
			TenantKey that = (TenantKey) o;
			return Objects.equals(this.tenantContext, that.tenantContext) &&
					Objects.equals(this.key, that.key) &&
					Objects.equals(this.cacheName, that.cacheName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.tenantContext, this.key, this.cacheName);
		}
	}
}