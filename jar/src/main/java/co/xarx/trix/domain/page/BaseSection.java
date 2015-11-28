package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class BaseSection extends BaseEntity implements Section, Serializable {

	@NotNull
	public String title;

	@NotNull
	public String layout;

	public void setTitle(String title) {
		this.title = title;
	}

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
}
