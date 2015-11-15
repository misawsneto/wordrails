package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.interfaces.Section;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseSection extends BaseEntity implements Section {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public Integer id;

	@Column
	private String title;

	@Column
	private String layout;

	@ManyToOne
	private Page page;

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
