package com.wordrails.business;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.ContainedIn;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Station {
	public static final String RESTRICTED = "RESTRICTED";
	public static final String RESTRICTED_TO_NETWORKS = "RESTRICTED_TO_NETWORKS";	
	public static final String UNRESTRICTED = "UNRESTRICTED";	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
		
	@Size(min=1, max=100)
	@NotNull
	public String name;

	@NotNull
	public boolean writable;
	
	@NotNull
	public boolean main = false;
	
	@NotNull	
	public String visibility;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSignup;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowComments;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSocialLogin;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSocialShare;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowWritersToNotify;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowWritersToAddSponsors;
	
	@Column(columnDefinition="varchar(255) default '#ffffff'")
	public String backgroundColor = "#ffffff";
	
	@Column(columnDefinition="varchar(255) default '#ffffff'")
	public String navbarColor = "#ffffff";
	
	@Column(columnDefinition="varchar(255) default '#5C78B0'")
	public String primaryColor = "#5C78B0";
	
	@Size(min=1)
	@NotNull
	@ManyToMany	
	public Set<Network> networks;
	
	@OneToMany(mappedBy="station", cascade=CascadeType.REMOVE)
	public Set<StationRole> personsStationRoles;

	@OneToMany(mappedBy="station")
	@ContainedIn
	public Set<Post> posts;
	
	@OneToMany(mappedBy="station", cascade=CascadeType.REMOVE)
	public Set<Promotion> promotions;	

	@Size(min=1)
	@NotNull	
	@OneToMany(mappedBy="station", cascade=CascadeType.PERSIST)
	public Set<StationPerspective> stationPerspectives;
	
	@OneToMany(mappedBy="owningStation", cascade=CascadeType.PERSIST)
	public Set<Taxonomy> ownedTaxonomies;
	
	public int postsTitleSize;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean topper;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean subheading;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean sponsored;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean overrideNetworkConfig;
	
	@OneToOne
	public Wordpress wordpress;
	
	@OneToOne
	public Image logo;
	
	public Integer logoId;
	
	public Integer defaultPerspectiveId;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		if(logo != null && logo.original != null){
			logoId = logo.original.id;
		}
	}
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
		if(logo != null && logo.original != null){
			logoId = logo.original.id;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
		result = prime * result + (writable ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (visibility == null) {
			if (other.visibility != null)
				return false;
		} else if (!visibility.equals(other.visibility))
			return false;
		if (writable != other.writable)
			return false;
		return true;
	}
}