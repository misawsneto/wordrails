package co.xarx.trix.aspect;

import co.xarx.trix.aspect.annotations.TimeIt;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProfilerAspect {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Around("@annotation(timeIt)")
	public Object profile(ProceedingJoinPoint pjp, TimeIt timeIt) throws Throwable {
		long start = System.currentTimeMillis();
		log.debug("@ profile - Calling method " + ((MethodSignature)pjp.getSignature()).getMethod());
		Object output = pjp.proceed();
		long finish = System.currentTimeMillis();
		long elapsedTime = finish - start;
		log.debug("@ profile - Finishing method at " + finish + " - Elapsed time: " + elapsedTime);
		return output;
	}

}
