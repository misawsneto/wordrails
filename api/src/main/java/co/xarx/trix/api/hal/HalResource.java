package co.xarx.trix.api.hal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalResource extends ResourceSupport {

	private final Map<String, ResourceSupport> embedded = new HashMap<>();

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("_embedded")
	public Map<String, ResourceSupport> getEmbeddedResources() {
		return embedded;
	}

	@JsonProperty("_links")
	public List<Link> getLinks() {
		return super.getLinks();
	}

	public void embedResource(String relationship, ResourceSupport resource) {

		embedded.put(relationship, resource);
	}
}
