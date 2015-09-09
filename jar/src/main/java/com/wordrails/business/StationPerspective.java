package com.wordrails.business;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="station_perspective")
public class StationPerspective {
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
	
	@PreUpdate
	private void onUpdate() {
		if(station != null){
			stationId = station.id;
		}
	}
	
	@PrePersist
	private void onCreate() {
		if(station != null){
			stationId = station.id;
		}
	}

	// TODO guardar o dono da perspectiva da estação
}