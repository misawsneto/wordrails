package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Setter;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "section")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
@JsonTypeInfo(
		use = JsonTypeInfo.Id.MINIMAL_CLASS,
		include = JsonTypeInfo.As.PROPERTY,
		property = "sectionType")
@JsonSubTypes({
		@JsonSubTypes.Type(value = QueryableListSection.class, name = "QueryableListSection"),
		@JsonSubTypes.Type(value = ContainerSection.class, name = "ContainerSection")
})
public abstract class AbstractSection extends BaseEntity implements Section, Serializable {

	private static final long serialVersionUID = 3219789942521409531L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	public String title;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "section_properties", joinColumns = @JoinColumn(name = "section_id"))
	@MapKeyColumn(name = "property_key", nullable = false, length = 100)
	@Column(name = "value", nullable = false)
	public Map<String, String> properties;

	public String orientation = Constants.Layout.SECTION_HORIZONTAL_ORIENTATION;

	@Min(value = 1)
	@Max(value = 100)
	public Integer pctSize;

	public Integer topMargin;
	public Integer leftMargin;
	public Integer bottomMargin;
	public Integer rightMargin;

	public Integer topPadding;
	public Integer leftPadding;
	public Integer bottomPadding;
	public Integer rightPadding;

	public Integer getPctSize() {
		return pctSize;
	}

	public void setPctSize(Integer pctSize) {
		this.pctSize = pctSize;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Integer getTopMargin() {
		return topMargin;
	}

	public void setMargin(Integer margin) {
		this.setTopMargin(margin);
		this.setLeftMargin(margin);
		this.setBottomMargin(margin);
		this.setRightMargin(margin);
	}

	public void setTopMargin(Integer topMargin) {
		this.topMargin = topMargin;
	}

	public Integer getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(Integer leftMargin) {
		this.leftMargin = leftMargin;
	}

	public Integer getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(Integer bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public Integer getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(Integer rightMargin) {
		this.rightMargin = rightMargin;
	}

	public Integer getTopPadding() {
		return topPadding;
	}

	public void setPadding(Integer padding) {
		this.setTopPadding(padding);
		this.setLeftPadding(padding);
		this.setBottomPadding(padding);
		this.setRightPadding(padding);
	}

	public void setTopPadding(Integer topPadding) {
		this.topPadding = topPadding;
	}

	public Integer getLeftPadding() {
		return leftPadding;
	}

	public void setLeftPadding(Integer leftPadding) {
		this.leftPadding = leftPadding;
	}

	public Integer getBottomPadding() {
		return bottomPadding;
	}

	public void setBottomPadding(Integer bottomPadding) {
		this.bottomPadding = bottomPadding;
	}

	public Integer getRightPadding() {
		return rightPadding;
	}

	public void setRightPadding(Integer rightPadding) {
		this.rightPadding = rightPadding;
	}

}
