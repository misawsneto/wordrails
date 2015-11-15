package co.xarx.trix.aspect;

import co.xarx.trix.domain.MultiTenantEntity;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class MultitenantRepositoryAspect {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Before("bean(*Repository) && execution(* *..save(*)) && args(entity)")
	public void checkMultitenantEntity(MultiTenantEntity entity) throws Throwable {
		if (entity != null && entity.getNetworkId() == null) {
			log.info("@ checkMultitenantEntity " + entity.getClass().getSimpleName());
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			Integer networkId = (Integer) attr.getAttribute("networkId", RequestAttributes.SCOPE_REQUEST);
			entity.setNetworkId(networkId);
			for(MultiTenantEntity mte : getFields(MultiTenantEntity.class, entity)) {
				checkMultitenantEntity(mte);
			}
			log.warn("@ checkMultitenantEntity - saving " + networkId);
		}
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
					if(classType.isAssignableFrom(Class.forName(genericType.toString().replace("class ", "")))) {
						toReturn.addAll((Collection<T>) f.get(obj));
					}
				}
			} else if(Map.class.isAssignableFrom(type)) {
				Map map = (Map) f.get(obj);
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

		return toReturn;
	}
}
