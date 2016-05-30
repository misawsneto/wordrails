package co.xarx.trix.config.multitenancy;

import org.hibernate.Session;
import org.hibernate.jpa.internal.EntityManagerImpl;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EntityManagerFactoryDynamicProxy implements InvocationHandler {

	private EntityManagerFactory emf;

	public EntityManagerFactoryDynamicProxy(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public static Object newInstance(EntityManagerFactory emf) {
		return Proxy.newProxyInstance(
				emf.getClass().getClassLoader(),
				emf.getClass().getInterfaces(),
				new EntityManagerFactoryDynamicProxy(emf)
		);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result;
		try {
			result = method.invoke(emf, args);
//			log.debug("invoke entity manager factory: " + method.getName());
			if(method.getName().equals("createEntityManager")) {
				Session session = ((EntityManagerImpl) result).getSession();

				String tenantId = TenantContextHolder.getCurrentTenantId();
				if(tenantId != null) {
					session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
				}
			}
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
		}

		return result;
	}
}
