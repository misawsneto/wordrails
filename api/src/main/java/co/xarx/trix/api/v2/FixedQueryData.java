package co.xarx.trix.api.v2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FixedQueryData {
	private List<Integer> indexes = new ArrayList<>();
	private AbstractStatementData statement;
}
