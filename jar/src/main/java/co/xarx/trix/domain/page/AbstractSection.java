package co.xarx.trix.domain.page;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
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
		@JsonSubTypes.Type(value = ContainerSection.class, name = "ContainerSection"),
		@JsonSubTypes.Type(value = LinkSection.class, name = "LinkSection")
})
public abstract class AbstractSection extends BaseEntity implements Section, Serializable {

	private static final long serialVersionUID = 3219789942521409531L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String title;

	@NotNull
	@Enumerated(EnumType.STRING)
	@SdkExclude
	public Style style;

	@NotNull
	@Range(min = 0)
	public Integer orderPosition;

	@NotNull
	@ManyToOne
	@SdkExclude
	public Page page;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "section_properties", joinColumns = @JoinColumn(name = "section_id"))
	@MapKeyColumn(name = "property_key", nullable = false, length = 100)
	@Column(name = "value", nullable = false)
	public Map<String, String> properties;

	public Orientation orientation = Orientation.HORIZONTAL;

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

	public void setMargin(Integer margin) {
		this.setTopMargin(margin);
		this.setLeftMargin(margin);
		this.setBottomMargin(margin);
		this.setRightMargin(margin);
	}

	public void setPadding(Integer padding) {
		this.setTopPadding(padding);
		this.setLeftPadding(padding);
		this.setBottomPadding(padding);
		this.setRightPadding(padding);
	}
}
