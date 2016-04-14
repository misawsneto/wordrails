package co.xarx.trix.web.rest.mapper;

public interface Mapper<S, K> {

	K convert(S s);
}
