package co.xarx.trix.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("properties")
public class TrixProperties {

	@Value("${spring.data.elasticsearch.index}") //important. don't delete
	public String index;

	@Value("${elasticsearch.access_index}")
	public String access_index;
}