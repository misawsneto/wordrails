package co.xarx.trix.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TenantAuthorize {

	String[] profiles();

	String[] tenants();

	boolean isGrant() default true;
}
