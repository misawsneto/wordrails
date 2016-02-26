package co.xarx.trix.domain.query;

import java.util.List;

public interface Executor<T, K extends Command> {

	List<T> execute(K command, Integer size, Integer from);
}
