package co.xarx.trix.domain.page;


import java.util.Map;

public interface Section {

	Map<String, String> getProperties();

	String getTitle();

	String getType();
}
