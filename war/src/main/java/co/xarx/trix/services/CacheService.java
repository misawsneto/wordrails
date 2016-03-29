package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheService {

	private CacheManager cacheManager;

	@Autowired
	public CacheService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void putWithTenantAsKey(String cacheName, Object object) {
		Cache postsIdsCache = cacheManager.getCache(cacheName);
		postsIdsCache.putIfAbsent(TenantContextHolder.getCurrentTenantId(), object);
	}
}
