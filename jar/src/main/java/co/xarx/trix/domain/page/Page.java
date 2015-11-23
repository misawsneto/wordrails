package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.Station;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Map;

@Entity
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public class Page extends BaseEntity {

	public String title;

	@OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
	@MapKeyJoinColumn(name = "index", referencedColumnName = "index", nullable = false)
	public Map<Integer, BaseSection> sections;

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
