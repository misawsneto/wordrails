package co.xarx.trix.persistence.repository;

import co.xarx.trix.api.v2.PostData;
import com.google.common.base.Charsets;
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
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

//import static co.xarx.trix.persistence.tables.Post.POST;

public class SQLPostRepositoryTest {

	private DSLContext create;

	class MockPostProvider implements MockDataProvider {

		@Override
		public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
			MockResult[] mock = new MockResult[1];

			String sql = ctx.sql();

			if (sql.toUpperCase().startsWith("DROP")) {
				throw new SQLException("Statement not supported: " + sql);
			}

			else if (sql.toUpperCase().startsWith("SELECT")) {
				URL url = Resources.getResource("jooq_postdata.json");
				try {
					String text = Resources.toString(url, Charsets.UTF_8);
					Result<Record> result = create.newResult();
//					result.add(create.newRecord(POST, TestArtifactsFactory.createPost()));
					mock[0] = new MockResult(1, result);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			return mock;
		}
	}


	@Test
	public void findByIds() throws Exception {
		MockDataProvider provider = new MockPostProvider();
		MockConnection connection = new MockConnection(provider);

		create = DSL.using(connection, SQLDialect.MYSQL);

		SQLPostRepository repository = new SQLPostRepository(create);
		List<PostData> data = repository.findByIds(Arrays.asList(1, 2, 3));
	}

}