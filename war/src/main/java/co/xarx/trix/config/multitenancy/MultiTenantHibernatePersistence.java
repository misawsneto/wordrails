package co.xarx.trix.config.multitenancy;

import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

public class MultiTenantHibernatePersistence extends HibernatePersistenceProvider {

	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		EntityManagerFactory emf = super.createContainerEntityManagerFactory(info, properties);
		return (EntityManagerFactory) EntityManagerFactoryDynamicProxy.newInstance(emf);
	}
}
