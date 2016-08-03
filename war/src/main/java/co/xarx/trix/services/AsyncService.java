package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class AsyncService {

	@Autowired
	public PersonRepository personRepository;

	@Autowired
	public PostService postService;

//	@Autowired
//	ElasticsearchTemplate elasticsearchTemplate;

	@Async
	public void run(Runnable runnable) {
		runnable.run();
	}

	@Async
	public void run(String tenantId, Runnable runnable) {
		TenantContextHolder.setCurrentTenantId(tenantId);
		runnable.run();
	}

	@Async
	public <V> Future<V> run(String tenantId, Callable<V> runnable) throws Exception {
		TenantContextHolder.setCurrentTenantId(tenantId);
		return new AsyncResult<>(runnable.call());
	}

	@Async
	public void sendNotification(String tenantId, Post post) {
		TenantContextHolder.setCurrentTenantId(tenantId);
		postService.sendNewPostNotification(post);
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
