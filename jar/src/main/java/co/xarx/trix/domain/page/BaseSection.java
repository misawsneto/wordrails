package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.interfaces.Section;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseSection extends BaseEntity implements Section, Serializable {

	public String title;

	public String layout;

	@ManyToOne
	public Page page;

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
	public Page getPage() {
		return page;
	}

	@Override
	public void setPage(Page page) {
		this.page = page;
	}
}