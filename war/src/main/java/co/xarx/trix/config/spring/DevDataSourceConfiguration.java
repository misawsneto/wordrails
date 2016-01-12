package co.xarx.trix.config.spring;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSourceConfiguration {

	@Bean
	public BoneCPDataSource dataSource() {
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost/trix_dev");
		dataSource.setUsername("wordrails");
		dataSource.setPassword("wordrails");

		return dataSource;
	}
}