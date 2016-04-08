package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.Station;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "page")
public class Page extends BaseEntity {

	public String title;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
	@MapKey(name = "orderPosition")
	private Map<Integer, AbstractSection> sections;

	@ManyToOne
	private Station station;

	public List<Section> getSectionList() {
		if (sections != null) {
			return new ArrayList<>(sections.values());
		} else {
			return new ArrayList<>();
		}
	}

	public void addSection(AbstractSection section) {
		if(sections == null)
			sections = new HashMap();

		sections.put(sections.size(), section);
	}
}
