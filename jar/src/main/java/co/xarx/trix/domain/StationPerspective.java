package co.xarx.trix.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name="station_perspective")
public class StationPerspective extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -8566727982028785563L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@Override
	public Serializable getId() {
		return id;
	}

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
}