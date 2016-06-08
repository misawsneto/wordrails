package co.xarx.trix.aspect;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.annotation.TimeIt;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@IntegrationTestBean
public class ProfilerAspect {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Around("@annotation(timeIt)")
	public Object profile(ProceedingJoinPoint pjp, TimeIt timeIt) throws Throwable {
		long start = System.currentTimeMillis();
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		Object output = pjp.proceed();
		long finish = System.currentTimeMillis();
		long elapsedTime = finish - start;
		String className = method.getDeclaringClass().getSimpleName();
		log.debug("@ profile - Method " + className + "." + method.getName() + "\n\t Elapsed time: " + elapsedTime);
		return output;
	}

}
