package co.xarx.trix.api.hal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

import java.util.*;

public class HalResources<T> extends ResourceSupport implements Iterable<T> {

	private final Collection<T> content;

	public HalResources(Iterable<T> content, Link... links) {
		this(content, Arrays.asList(links));
	}

	public HalResources(Iterable<T> content, Iterable<Link> links) {

		Assert.notNull(content);

		this.content = Lists.newArrayList(content);
		this.add(links);
	}

	@JsonProperty("content")
	public Collection<T> getContent() {
		return Collections.unmodifiableCollection(content);
	}

	@JsonProperty("_links")
	public List<Link> getLinks() {
		return super.getLinks();
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public String toString() {
		return String.format("Resources { content: %s, %s }", getContent(), super.toString());
	}

	public static <T extends HalResource, S> HalResources wrap(Iterable<S> content, ResourceAssembler<S, T> assembler) {

		Assert.notNull(content);
		ArrayList<T> resources = new ArrayList<>();

		for (S element : content) {
			resources.add(assembler.toResource(element));
		}

		return new HalResources(resources);
	}
}
