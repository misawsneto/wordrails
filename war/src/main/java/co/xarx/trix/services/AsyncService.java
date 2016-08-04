package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Component
public class AsyncService {

	@Autowired
	public PersonRepository personRepository;

//	@Autowired
//	ElasticsearchTemplate elasticsearchTemplate;

	@Async(value = "myExecuter")
	public void run(Runnable runnable) {
		runnable.run();
	}

	@Async(value = "myExecuter")
	public void run(String tenantId, Runnable runnable) {
		TenantContextHolder.setCurrentTenantId(tenantId);
		runnable.run();
	}

	@Async(value = "myExecuter")
	public <V> Future<V> run(String tenantId, Callable<V> runnable) throws Exception {
		TenantContextHolder.setCurrentTenantId(tenantId);
		return new AsyncResult<>(runnable.call());
	}

//	@Async
//	@Transactional
//	public void updatePersonLastLoginDate(String tenantId, String username) {
//		Logger.info(TenantContextHolder.getCurrentTenantId());
//		TenantContextHolder.setCurrentTenantId(tenantId);
//		Person person = personRepository.findByUsernameAndTenantId(username, tenantId);
//		if (person != null) {
//			person.lastLogin = new Date();
//			personRepository.save(person);
//		}
//	}

//	@Async
//	public void asyncBulkSaveIndex(List<IndexQuery> queries) {
//		elasticsearchTemplate.bulkIndex(queries);
//	}
}
