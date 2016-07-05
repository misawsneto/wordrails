EntityDto(package) ::=

<<package $package$;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityDto implements Serializable {
	private static final long serialVersionUID = -7723825842523034233L;

	public List<Link> links;

	void setSelf(String href) {
		if (links == null) {
			links = new ArrayList<Link>();
		}
		Link self = null;
		for (Link link : links) {
			String rel = link.rel;
			if ("self".equals(rel)) {
				self = link;
			}
		}
		if (self == null) {
			self = new Link();
			self.rel = "self";
			links.add(self);
		}
		self.href = href;
	}
	
	public String getSelf() {
		if(links != null)
			for (Link link : links) {
				String rel = link.rel;
				if ("self".equals(rel)) {
					return link.href;
				}
			}
		else
			return "";
		throw new RuntimeException();
	}
}>>