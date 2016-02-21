package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "section")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
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
	@MapKeyColumn(name = "property_key", nullable = false)
	@Column(name = "value", nullable = false)
	public Map<String, String> properties;
}
