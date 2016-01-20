package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
		@JsonSubTypes.Type(value = BlockImpl.class, name = "blockimpl")
})
@JsonPropertyOrder({ "objectType", "object" })
public interface Block<T extends Identifiable> {

	String getObjectType();

	T getObject();
}
