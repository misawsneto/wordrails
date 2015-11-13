package co.xarx.trix.services.querydsl;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.sql.SQLQuery;
import org.apache.lucene.search.IndexSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public abstract class AbstractQueryDslService implements QueryDslService {

	private QueryDslJdbcTemplate jdbcTemplate;

	@Autowired(required=false)
	private IndexSearcher indexSearcher;

	protected SQLQuery sqlQuery() {
		return jdbcTemplate.newSqlQuery();
	}

	protected LuceneQuery luceneQuery() {
		return new LuceneQuery(indexSearcher);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new QueryDslJdbcTemplate(dataSource);
	}

}
