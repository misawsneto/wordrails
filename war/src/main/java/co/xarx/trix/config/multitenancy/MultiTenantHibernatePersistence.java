package co.xarx.trix.config.multitenancy;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class MultiTenantHibernatePersistence extends HibernatePersistenceProvider {

	@Autowired
	HttpServletRequest proxy;

	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		EntityManagerFactory emf = super.createContainerEntityManagerFactory(info, properties);
		return (EntityManagerFactory) EntityManagerFactoryDynamicProxy.newInstance(emf);
	}
}
