package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

	@Async
	public void run(Runnable runnable) {
		runnable.run();
	}

	@Async
	public void run(String tenantId, Runnable runnable) {
		TenantContextHolder.setCurrentTenantId(tenantId);
		runnable.run();
	}
}
