package co.xarx.trix.config.spring;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSourceConfiguration {

	@Value("#{systemProperties['dbName'] ?: 'trix_dev'}")
	private String dbName;

	@Value("#{systemProperties['username'] ?: 'wordrails'}")
	private String username;

	@Value("#{systemProperties['password'] ?: 'wordrails'}")
	private String password;

	@Bean
	public BoneCPDataSource dataSource() {
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost/" + dbName);
		dataSource.setUsername(username);
		dataSource.setPassword(username);

		return dataSource;
	}
}