package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "section_base")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class BaseSection extends BaseEntity implements Section, Serializable {

	@NotNull
	public String title;

	@NotNull
	public String sectionLayout;

	@NotNull
	public String blockLayout;

	@Override
	public String getBlockLayout() {
		return blockLayout;
	}

	public void setBlockLayout(String blockLayout) {
		this.blockLayout = blockLayout;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSectionLayout(String layout) {
		this.sectionLayout = layout;
	}

	@Override
	public String getSectionLayout() {
		return sectionLayout;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
