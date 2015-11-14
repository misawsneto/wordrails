package co.xarx.trix.domain.page.interfaces;


import java.util.List;

public interface Page<T extends Section> {

	String getTitle();

	List<T> getSections();

	void setTitle(String title);

	void setSections(List<T> sections);
}
