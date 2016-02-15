package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class AsyncService {

	@Autowired
	public PersonRepository personRepository;

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
	@Transactional
	public void updatePersonLastLoginDate(String tenantId, String username) {
		TenantContextHolder.setCurrentTenantId(tenantId);
		Person person = personRepository.findByUsername(username);
		if (person != null) {
			person.lastLogin = new Date();
			personRepository.save(person);
		}
	}
}
