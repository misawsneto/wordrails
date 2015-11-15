package co.xarx.trix.domain.page.interfaces;


import co.xarx.trix.domain.page.Page;

public interface Section {

	String getTitle();

	String getType();

	String getLayout();

	Page getPage();

	void setPage(co.xarx.trix.domain.page.Page page);

	void setTitle(String title);

	void setLayout(String style);
}
