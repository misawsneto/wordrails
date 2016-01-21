package co.xarx.trix.config.spring;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdDataSourceConfiguration {

	@Value("#{systemProperties['dbName'] ?: 'wordrails4'}")
	private String dbName;

	@Value("#{systemProperties['username']}")
	private String username;

	@Value("#{systemProperties['password']}")
	private String password;

	@Bean
	public BoneCPDataSource dataSource() {
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://dbinstance1/" + dbName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}
}