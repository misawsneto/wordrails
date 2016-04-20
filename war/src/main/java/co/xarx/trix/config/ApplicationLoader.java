package co.xarx.trix.config;

import co.xarx.trix.services.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private InitService initService;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		initService.systemInit();
	}
}