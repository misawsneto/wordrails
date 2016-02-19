package co.xarx.trix.domain.query.statement;

import java.io.Serializable;

public interface Statement {

	void addIdExclusion(Serializable id);
}
