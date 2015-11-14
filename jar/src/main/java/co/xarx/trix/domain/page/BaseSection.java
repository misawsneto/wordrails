package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.interfaces.Page;
import co.xarx.trix.domain.page.interfaces.Section;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseSection extends BaseEntity implements Section {

	private String title;

	private String layout;

	@ManyToOne
	private BasePage page;

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setLayout(String layout) {
		this.layout = layout;
	}

	@Override
	public String getLayout() {
		return layout;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public BasePage getPage() {
		return page;
	}

	@Override
	public void setPage(Page page) {
		this.page = (BasePage) page;
	}
}
