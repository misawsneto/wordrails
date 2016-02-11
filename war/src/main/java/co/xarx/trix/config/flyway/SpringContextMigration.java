package co.xarx.trix.config.flyway;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class SpringContextMigration implements SpringJdbcMigration {

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		FlywaySpringContextMigrator.autowire(this);
		migrate();
	}

	public abstract void migrate() throws Exception;

}