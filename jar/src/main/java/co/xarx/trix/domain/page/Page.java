package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.GeneratorIgnore;
import co.xarx.trix.domain.Station;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;

@Entity
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public class Page extends BaseEntity {

	public String title;

	@JsonIgnore
	@GeneratorIgnore
	@ManyToMany
	@JoinTable(name = "page_section",
			joinColumns = @JoinColumn(name = "page_id"),
			inverseJoinColumns = @JoinColumn(name = "section_id"))
	@MapKeyJoinColumn(name = "list_index")
	public Map<Integer, BaseSection> sections;

	@JsonProperty("sections")
	public Collection<BaseSection> getSectionList() {
		return sections.values();
	}

	@ManyToOne
	@JsonBackReference("station")
	public Station station;

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<Integer, BaseSection> getSections() {
		return sections;
	}

	public void setSections(Map<Integer, BaseSection> sections) {
		this.sections = sections;
	}
}
