package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
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

	public String title;

	@NotNull
	@Enumerated(EnumType.STRING)
	public Style style;

	@NotNull
	@Range(min = 0)
	public Integer orderPosition;

	@NotNull
	@ManyToOne
	public Page page;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "section_properties", joinColumns = @JoinColumn(name = "section_id"))
	@MapKeyColumn(name = "property_key", nullable = false, length = 100)
	@Column(name = "value", nullable = false)
	public Map<String, String> properties;
}
