package co.xarx.trix.config.spring;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSourceConfiguration {

	@Value("#{systemProperties.dbName}")
	private String dbName;

	@Bean
	public BoneCPDataSource dataSource() {
		if(dbName == null || dbName.isEmpty()) dbName = "trix_dev";

		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost/" + dbName);
		dataSource.setUsername("wordrails");
		dataSource.setPassword("wordrails");

		return dataSource;
	}
}