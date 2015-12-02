package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "section_base")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class BaseSection extends BaseEntity implements Section, Serializable {

	@NotNull
	public String title;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "section_properties", joinColumns = @JoinColumn(name = "section_id"))
	@MapKeyColumn(name = "key", nullable = false)
	@Column(name = "value", nullable = false)
	public Map<String, String> properties;

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
