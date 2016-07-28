package co.xarx.trix.domain.page;


import co.xarx.trix.domain.Identifiable;

import java.util.List;
import java.util.Map;

public interface Section extends Identifiable {

	enum Style {
		CARROUSEL, VERTICAL_LIST, HORIZONTAL_LIST, GRID
	}

	enum Orientation {
		HORIZONTAL, VERTICAL
	}

	enum Type {
		QUERYABLE, CONTAINER, LINK
	}

	Map<String, String> getProperties();

	String getTitle();

	Type getType();

	Style getStyle();

	Page getPage();

	List<Block> getBlocks();

	Orientation getOrientation();
}
