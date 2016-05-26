package co.xarx.trix.persistence.repository;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.domain.page.query.statement.PersonStatement;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SQLPersonRepositoryTest {

	private DSLContext create;

	class MockPostProvider implements MockDataProvider {

		@Override
		public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
			MockResult[] mock = null;

			String sql = ctx.sql();

			if (sql.toUpperCase().startsWith("DROP")) {
				throw new SQLException("Statement not supported: " + sql);
			}

			else if (sql.toUpperCase().startsWith("SELECT")) {
				URL url = Resources.getResource("jooq/persondata.json");
				try {
					Result<Record> records = create.fetchFromJSON(Resources.toString(url, Charsets.UTF_8));
					mock = new MockResult[records.size()];
					for (int i = 0; i < records.size(); i++) {
						mock[i] = new MockResult(records.get(i));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			return mock;
		}
	}


	@Test
	@Ignore
	public void findByIds() throws Exception {
		MockDataProvider provider = new MockPostProvider();
		MockConnection connection = new MockConnection(provider);

		create = DSL.using(connection, SQLDialect.MYSQL);

		PersonStatement params = new PersonStatement();
		params.setEmails(Lists.newArrayList("arthur.hvt@gmail.com", "misawsneto@gmail.com"));
		SQLPersonRepository repository = new SQLPersonRepository(create);
		List<PersonData> data = repository.findAll(params, 10, 1, null);

		data.forEach(System.out::println);

		assertEquals(10, data.size());
	}

}