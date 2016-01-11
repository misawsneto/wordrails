package co.xarx.trix.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="station_perspective")
public class StationPerspective implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@Size(min=1, max=100)
	public String name;

	@NotNull
	@ManyToOne
	public Station station;

	@ManyToOne
	public Taxonomy taxonomy;

	@OneToMany(mappedBy="perspective", cascade=CascadeType.REMOVE)
	public Set<TermPerspective> perspectives;
	
	public Integer stationId;
	public Integer taxonomyId;

	public String taxonomyName;
	public String taxonomyType;

	@PreUpdate
	private void onUpdate() {
		if(station != null){
			stationId = station.id;
			taxonomyId = taxonomy.id;
			taxonomyName = taxonomy.name;
			taxonomyType = taxonomy.type;
		}
	}
	
	@PrePersist
	private void onCreate() {
		if(station != null){
			stationId = station.id;
			taxonomyId = taxonomy.id;
			taxonomyName = taxonomy.name;
			taxonomyType = taxonomy.type;
		}
	}

	// TODO guardar o dono da perspectiva da estação
}