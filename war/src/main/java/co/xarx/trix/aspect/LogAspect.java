package co.xarx.trix.aspect;

import co.xarx.trix.domain.MultiTenantEntity;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LogAspect {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Before("bean(postRepository) && execution(* *..save(*)) && args(entity)")
	public void logSave(MultiTenantEntity entity){
		log.info("--------------------------------- SAVED ---------------------------------");
		System.out.println("--------------------------------- DELETED ---------------------------------11");
	}

	@Before("execution(postRepository)")
	public void logUpdate(){
		log.info("--------------------------------- UPDATED ---------------------------------");
		System.out.println("--------------------------------- DELETED ---------------------------------11");
	}
}
