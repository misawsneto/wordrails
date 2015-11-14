package co.xarx.trix.aspect;

import co.xarx.trix.domain.MultiTenantEntity;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class MultitenantRepositoryAspect {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Context
	HttpServletRequest request;

	@Before("bean(*Repository) && execution(* *..save(*)) && args(entity)")
	public void checkMultitenantEntity(MultiTenantEntity entity) throws Throwable {
		log.info("@ checkMultitenantEntity " + entity.getClass().getSimpleName());
		if (entity.getNetworkId() == null) {
			log.warn("@ checkMultitenantEntity - not saved");
			Integer networkId = (Integer) request.getSession().getAttribute("networkId");
			entity.setNetworkId(networkId);
			log.warn("@ checkMultitenantEntity - saving " + networkId);
		} else {
			log.debug("@ entity is already saved");
		}
	}
}
