package co.xarx.trix.api.v2;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ContainerSectionData extends SectionData {

	Integer count = 0;
	private Map<Integer, SectionData> children;

	public void add(SectionData section) {
		if(children == null)
			children = new LinkedHashMap<>();

		children.put(count++, section);
	}
}
