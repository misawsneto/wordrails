package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.Station;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "page")
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public class Page extends BaseEntity {

	public String title;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "page_section",
			joinColumns = @JoinColumn(name = "page_id"),
			inverseJoinColumns = @JoinColumn(name = "section_id"))
	@MapKeyJoinColumn(name = "list_index")
	public Map<Integer, AbstractSection> sections;

	@JsonProperty("sections")
	public Collection<AbstractSection> getSectionList() {
		return sections.values();
	}

	@ManyToOne
	@JsonBackReference("station")
	public Station station;
}
