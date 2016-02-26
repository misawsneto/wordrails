package co.xarx.trix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("properties")
public class TrixProperties {


	@Value("${spring.data.elasticsearch.index}") //important. don't delete
	public String index;
}