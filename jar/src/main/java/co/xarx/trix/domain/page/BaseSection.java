package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class BaseSection extends BaseEntity implements Section, Serializable {

	@JsonIgnore
	@NotNull
	public Integer index;

	@NotNull
	public String title;

	@NotNull
	public String layout = Constants.Layout.VERTICAL_LIST;

	@ManyToOne
	@JsonBackReference("page")
	public Page page;

	@Override
	public Integer getIndex() {
		return index;
	}

	@Override
	public void setIndex(Integer index) {
		this.index = index;
	}

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
