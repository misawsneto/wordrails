package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.Station;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "page")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Page extends BaseEntity {

	public String title;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
	@MapKey(name = "orderPosition")
	private Map<Integer, AbstractSection> sections;

	@NotNull
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
