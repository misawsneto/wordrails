package co.xarx.trix.config.flyway;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class SpringContextMigration implements SpringJdbcMigration {

	protected JdbcTemplate jdbc;

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		this.jdbc = jdbcTemplate;
		FlywayIntegrator.autowire(this);
		migrate();
	}

	public abstract void migrate() throws Exception;
}