package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Page extends BaseEntity {

	public String title;

	@OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
	public List<BaseSection> sections;

	@ManyToOne
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

	public List<BaseSection> getSections() {
		return sections;
	}

	public void setSections(List<BaseSection> sections) {
		this.sections = sections;
	}
}
