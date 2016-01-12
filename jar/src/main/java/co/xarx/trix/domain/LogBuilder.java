package co.xarx.trix.domain;

public interface LogBuilder {

	PostEvent build(String type, Post query);

//	EventEntity build(String type, Person query);
}
