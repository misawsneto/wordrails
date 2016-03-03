package co.xarx.trix.domain.page.query;

public interface ExecutorFactory<T extends Executor> {

	T getExecutor(String alias);
}
