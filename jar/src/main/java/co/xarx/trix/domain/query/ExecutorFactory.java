package co.xarx.trix.domain.query;

public interface ExecutorFactory<T extends Executor> {

	T getExecutor(String alias);
}
