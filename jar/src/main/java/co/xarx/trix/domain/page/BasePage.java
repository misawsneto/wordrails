package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.interfaces.Page;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasePage extends BaseEntity implements Page<BaseSection> {

	private String title;

	@OneToMany(mappedBy = "page")
	private List<BaseSection> sections;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public List<BaseSection> getSections() {
		return sections;
	}

	@Override
	public void setSections(List<BaseSection> sections) {
		this.sections = sections;
	}
}
