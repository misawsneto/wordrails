package co.xarx.trix.domain.page;


import co.xarx.trix.domain.Identifiable;

import java.util.List;
import java.util.Map;

public interface Section extends Identifiable {

	public enum Style {
		CARROUSEL, VERTICAL_LIST, HORIZONTAL_LIST, GRID;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	Map<String, String> getProperties();

	String getTitle();

	String getType();

	Style getStyle();

	Page getPage();

	List<Block> getBlocks();
}
