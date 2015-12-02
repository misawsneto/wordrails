package co.xarx.trix.config.spring;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
public interface DataSourceConfig {

	DataSource dataSource();

	@Configuration
	@Profile("dev")
	class StandaloneDataSourceConfiguration implements DataSourceConfig {

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

	@Configuration
	@Profile("prod")
	class JndiDataSourceConfiguration implements DataSourceConfig {

		@Bean
		public DataSource dataSource() {
			try {
				Context ctx = new InitialContext();
				return (DataSource) ctx.lookup("jdbc/trix");
			} catch (NamingException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

}