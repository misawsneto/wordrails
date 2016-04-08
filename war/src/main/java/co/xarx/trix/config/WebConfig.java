package co.xarx.trix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class WebConfig extends WebMvcConfigurationSupport {

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer c) {
		c.defaultContentType(MediaTypes.HAL_JSON);
	}
}