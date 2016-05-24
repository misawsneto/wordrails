//package co.xarx.trix.persistence.repository;
//
//import com.jolbox.bonecp.BoneCPDataSource;
//import org.jooq.DSLContext;
//import org.jooq.SQLDialect;
//import org.jooq.impl.DataSourceConnectionProvider;
//import org.jooq.impl.DefaultConfiguration;
//import org.jooq.impl.DefaultDSLContext;
//
//import javax.sql.DataSource;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static co.xarx.trix.jooq.tables.Person.PERSON;
//
//public class CreateJOOQFile {
//
//	private static final String FILE_NAME = "persondata.json";
//
//	public static void main(String[] args) throws IOException {
//		CreateJOOQFile script = new CreateJOOQFile();
//		script.createFile();
//	}
//
//	public void createFile() throws IOException {
//		File path = new File("war/src/main/resources/jooq/");
//
//		String json = dslContext().select().from(PERSON).fetch().formatJSON();
//
//		if (!path.exists()) {
//			path.mkdirs();
//		}
//		Path file = Paths.get("war/src/main/resources/jooq/" + FILE_NAME);
//		Path write = Files.write(file, json.getBytes());
//
//		System.out.println("Script created sucessfully");
//	}
//
//	public DataSource dataSource() {
//		BoneCPDataSource ds = new BoneCPDataSource();
//		ds.setDriverClass("com.mysql.jdbc.Driver");
//		ds.setJdbcUrl("jdbc:mysql://localhost:3306/trix_dev");
//		ds.setUsername("wordrails");
//		ds.setPassword("wordrails");
//		ds.setPartitionCount(3);
//		ds.setMinConnectionsPerPartition(3);
//		ds.setMaxConnectionsPerPartition(128);
//		ds.setAcquireIncrement(5);
//		ds.setIdleMaxAgeInSeconds(60);
//		ds.setMaxConnectionAgeInSeconds(120);
//
//		return ds;
//	}
//
//	public DataSourceConnectionProvider dataSourceConnectionProvider() {
//		return new DataSourceConnectionProvider(dataSource());
//	}
//
//	public DefaultConfiguration defaultConfiguration() {
//		DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
//		defaultConfiguration.setConnectionProvider(dataSourceConnectionProvider());
//		defaultConfiguration.setSQLDialect(SQLDialect.MYSQL);
//		return defaultConfiguration;
//	}
//
//	public DSLContext dslContext() {
//		return new DefaultDSLContext(defaultConfiguration());
//	}
//}