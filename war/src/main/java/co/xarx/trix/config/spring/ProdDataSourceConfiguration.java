package co.xarx.trix.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class ProdDataSourceConfiguration {

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