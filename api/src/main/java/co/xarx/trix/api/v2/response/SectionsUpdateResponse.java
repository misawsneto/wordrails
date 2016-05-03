package co.xarx.trix.api.v2.response;

import java.util.List;

public class SectionsUpdateResponse {

	public List<Section> sections;

	public class Section {
		public Integer id;
	}

	public class ContainerSection extends Section {
		public List<Section> children;
	}
}
