package co.xarx.trix.api.v2.response;

import java.util.List;

public class SaveSectionsResponse {

	public List<Section> sections;

	public class Section {
		public Integer id;
	}

	public class ContainerSection extends Section {
		public List<Section> children;
	}
}
