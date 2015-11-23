package co.xarx.trix.domain.page;


public interface Section {

	String getTitle();

	String getType();

	String getLayout();

	Page getPage();

	void setPage(Page page);

	Integer getIndex();

	void setIndex(Integer index);

	void setTitle(String title);

	void setLayout(String style);
}
