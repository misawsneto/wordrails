package co.xarx.trix.config.cache;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.exception.TargetLookupFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

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

	private final Logger logger = LoggerFactory.getLogger(MultitenantCache.class);

	public final Cache delegate;


	public MultitenantCache(final Cache delegate) {
		this.delegate = delegate;
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
	public <T> T get(Object key, Callable<T> callable) {
		ValueWrapper val = delegate.get(key);
		if (val != null) {
			return (T) val.get();
		}

		try {
			return  callable.call();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void put(Object key, Object value) {
		Object translatedKey = translateKey(key);
		this.delegate.put(translatedKey, value);
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

	private String translateKey(Object key) throws TargetLookupFailureException {
		Assert.notNull(key, "Key must have some value");

		logger.trace("Translating key {}", key);
		String tenantContext = TenantContextHolder.getCurrentTenantId();

		Assert.hasText(tenantContext, "Tenant context is required but is not available");

		return tenantContext + ":" + this.getName() + ":" + key;
	}
}