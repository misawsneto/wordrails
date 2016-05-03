package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.AbstractStatementData;
import co.xarx.trix.api.v2.PostStatementData;
import co.xarx.trix.domain.page.query.statement.AbstractStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbstractStatementMapper {

	@Autowired
	private PostStatementMapper postStatementMapper;

	public AbstractStatement asEntity(AbstractStatementData abstractStatementData) {
		if (abstractStatementData instanceof PostStatementData) {
			return postStatementMapper.asEntity((PostStatementData) abstractStatementData);
		}

		return null;
	}
}
