package co.xarx.trix.aspect;

import co.xarx.trix.aspect.annotations.IgnoreMultitenancy;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.MultiTenantEntity;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Aspect
@Component
public class MultitenantRepositoryAspect {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Before("within(org.springframework.data.repository.CrudRepository+) && execution(* *..save(*)) && args(entity)")
	public void checkMultitenantEntity(MultiTenantEntity entity) throws Throwable {
		if (entity != null && entity.getTenantId() == null) {
			String tenantId = TenantContextHolder.getCurrentTenantId();
			entity.setTenantId(tenantId);
			for(MultiTenantEntity mte : getFields(MultiTenantEntity.class, entity)) {
				checkMultitenantEntity(mte);
			}
			log.info("@ checkMultitenantEntity " + entity.getClass().getSimpleName() + " - saving " + tenantId);
		}
	}

	@Around("@annotation(ignoreMultitenancy)")
	public Object profile(ProceedingJoinPoint pjp, IgnoreMultitenancy ignoreMultitenancy) throws Throwable {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		TenantContextHolder.setCurrentTenantId(null);
		Object output = pjp.proceed();
		TenantContextHolder.setCurrentTenantId(tenantId);
		return output;
	}

	public <T> List<T> getFields(Class<T> classType, Object obj) throws IllegalAccessException, ClassNotFoundException {
		List<T> toReturn = new ArrayList<>();

		Field[] allFields = obj.getClass().getDeclaredFields();

		for (Field f : allFields) {
			Class<?> type = f.getType();
			if (classType.isAssignableFrom(type)) {
				toReturn.add((T) f.get(obj));
			} else if(Iterable.class.isAssignableFrom(type)) {
				for (Type genericType : ((ParameterizedType) f.getGenericType()).getActualTypeArguments()) {
					if(classType.isAssignableFrom(Class.forName(genericType.toString()
							.replace("class ", "").replace("interface ", "")))) {
						Object o = f.get(obj);
						if(o != null) toReturn.addAll((Collection<T>) o);
					}
				}
			} else if(Map.class.isAssignableFrom(type)) {
				Map map = (Map) f.get(obj);
				if (map != null) {
					for(Object o : map.keySet()) {
						if(classType.isAssignableFrom(o.getClass())) {
							toReturn.add((T) o);
						} else break;
					}
					for(Object o : map.values()) {
						if(classType.isAssignableFrom(o.getClass())) {
							toReturn.add((T) o);
						} else break;
					}
				}
			}
		}

		return toReturn;
	}
}