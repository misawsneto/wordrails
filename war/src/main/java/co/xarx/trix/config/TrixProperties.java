package co.xarx.trix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("properties")
public class TrixProperties {


	@Value("${elasticsearch.index}")
	public String index;
}
