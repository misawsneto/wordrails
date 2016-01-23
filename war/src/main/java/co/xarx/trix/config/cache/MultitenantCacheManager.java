package co.xarx.trix.config.cache;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.exception.TargetLookupFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link CacheManager} implementation that supports multi-tenancy by wrapping around another
 * {@link CacheManager} implementation, the delegate, and decorating caches returned by the delegate
 * with {@link MultitenantCache}.
 *
 * @author Joe Laudadio (Joe.Laudadio@AltegraHealth.com)
 */
public final class MultitenantCacheManager implements CacheManager {

	private Map<String, Cache> redisCaches = new HashMap<>();

	public MultitenantCacheManager(Map<String, RedisTemplate> redisTemplates) {
		redisTemplates.values().stream().forEach(RedisTemplate::afterPropertiesSet);

		redisTemplates.keySet().stream().forEach(cacheName ->
				redisCaches.put(cacheName, new MultitenantCache(new RedisCache(cacheName, null, redisTemplates.get(cacheName), 60)))
		);
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = redisCaches.get(name);
		String tenantContext = getTenantContext();
		if (tenantContext == null) {
			throw new TargetLookupFailureException("Tenant context required but not available");
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return redisCaches.keySet();
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