package co.xarx.trix.api.v2;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public abstract class AbstractStatementData {

	private Map<String, Boolean> sorts;
	private Set<Serializable> exceptionIds;

	public AbstractStatementData() {
		sorts = new HashMap<>();
		exceptionIds = new HashSet<>();
	}
}
