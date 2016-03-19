package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
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
}
