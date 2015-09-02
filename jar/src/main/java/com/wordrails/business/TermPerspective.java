package com.wordrails.business;

import java.util.List;
import java.util.Set;

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

	@OneToOne(mappedBy="featuringPerspective", cascade=CascadeType.REMOVE)
	public Row featuredRow;
	
	@OneToMany(mappedBy="perspective", cascade=CascadeType.REMOVE)
	public List<Row> rows;

	@ManyToMany
	public Set<Term> categoryTabs;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="station_perspective_id")
	public StationPerspective perspective;

	@ManyToOne
	@JoinColumn(name="term_id")
	public Term term;
	
	public Integer stationId;
	
	@PreUpdate
	private void onUpdate() {
		if(perspective != null && perspective.station != null){
			stationId = perspective.station.id;
		}
	}
	
	@PrePersist
	private void onCreate() {
		if(perspective != null && perspective.station != null){
			stationId = perspective.station.id;
		}
	}
}