package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Page extends BaseEntity {

	@Column
	private String title;

	@OneToMany(mappedBy = "page")
	private List<BaseSection> sections;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<BaseSection> getSections() {
		return sections;
	}

	public void setSections(List<BaseSection> sections) {
		this.sections = sections;
	}
}
