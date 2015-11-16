package co.xarx.trix.domain.page.interfaces;

public interface Block<T> {

	String POST_BLOCK = "post_block";

	T getObject();

	String getType();
}
