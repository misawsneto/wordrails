package co.xarx.trix.config;

import co.xarx.trix.elasticsearch.mapper.PersonMap;
import co.xarx.trix.elasticsearch.mapper.PostMap;
import co.xarx.trix.elasticsearch.mapper.PostViewMap;
import co.xarx.trix.elasticsearch.mapper.StationMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
//@ComponentScan(
//		basePackages = {"co.xarx.trix.aspect"},
//		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "co.xarx.trix.config.spring.*")
//)
public class ApplicationTestConfig {

	static {
		System.setProperty("indexES", "false");
		System.setProperty("dbName", "trix_dev");
	}

	@Bean
	public ObjectMapper simpleMapper() {
		return new ObjectMapper();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PersonMap());
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
		modelMapper.addMappings(new PostViewMap());
		return modelMapper;
	}
}
