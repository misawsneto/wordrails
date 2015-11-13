package co.xarx.trix.domain;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="term_perspective",uniqueConstraints = {@UniqueConstraint(columnNames={"station_perspective_id","term_id"})})
public class TermPerspective {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;

	@OneToOne(mappedBy="splashedPerspective", cascade=CascadeType.REMOVE)
	public Row splashedRow;

	@OneToMany(mappedBy="homePerspective", cascade=CascadeType.REMOVE)
	public List<Row> homeRows;

	@OneToOne(mappedBy="featuringPerspective", cascade=CascadeType.REMOVE)
	public Row featuredRow;
	
	@OneToMany(mappedBy="perspective", cascade=CascadeType.REMOVE)
	public List<Row> rows;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean showPopular;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean showRecent;

	@NotNull
	@ManyToOne
	@JoinColumn(name="station_perspective_id")
	public StationPerspective perspective;

	@ManyToOne
	@JoinColumn(name="term_id")
	public Term term;

	public Integer taxonomyId;

	public Integer stationId;

	public String defaultImageHash;
	
	@PreUpdate
	private void onUpdate() {
		if(perspective != null && perspective.station != null){
			stationId = perspective.station.id;
			taxonomyId = perspective.taxonomy.id;
		}
	}
	
	@PrePersist
	private void onCreate() {
		if(perspective != null && perspective.station != null){
			stationId = perspective.station.id;
			taxonomyId = perspective.taxonomy.id;
		}
	}
}