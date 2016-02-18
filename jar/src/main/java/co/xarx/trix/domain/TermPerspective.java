package co.xarx.trix.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="term_perspective",uniqueConstraints = {@UniqueConstraint(columnNames={"station_perspective_id","term_id"})})
public class TermPerspective extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@OneToOne(mappedBy="splashedPerspective", cascade=CascadeType.REMOVE)
	public Row splashedRow;

	@OneToOne(mappedBy="homePerspective", cascade=CascadeType.REMOVE)
	public Row homeRow;

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