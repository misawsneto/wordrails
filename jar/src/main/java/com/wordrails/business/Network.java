package com.wordrails.business;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Network implements Serializable{

	private static final long serialVersionUID = 7723825842358687233L;

//	public String NIGHT_READ_MODE = "N";
//	public String DAY_READ_MODE = "D";
//	public String VERTICAL_ORIENTATION_MODE = "V";
//	public String HORIZONTAL_ORIENTATION_MODE = "H";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@Size(min=1, max=100)
	public String name;
	
	@Size(min=1, max=100)
	public String flurryKey;
	
	@Size(min=1, max=100)	
	public String trackingId;

	@OneToMany(mappedBy="network")
	public Set<NetworkRole> personsNetworkRoles;

//	@OneToMany(mappedBy="network")
//	public Set<Person> persons;

	@ManyToMany(mappedBy="networks")
	public Set<Station> stations;

	@ManyToMany
	public Set<Taxonomy> taxonomies;

	@OneToMany(mappedBy="network", cascade=CascadeType.ALL)
	public Set<Sponsor> sponsors;

	@OneToMany(mappedBy="network")
	public Set<User> users;
		
	@OneToMany(mappedBy="owningNetwork")
	public Set<Taxonomy> ownedTaxonomies;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSignup;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSocialLogin;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSponsors;
	
	public String domain;

	@Lob
	public String loginFooterMessage;
	
//	@Column(columnDefinition="TEXT default '#F3F5F9'")
	public String backgroundColor = "#F3F5F9";
//	@Column(columnDefinition="TEXT default '#242424'")
	public String navbarColor = "#242424";
//	@Column(columnDefinition="TEXT default '#505050'")
	public String navbarSecondaryColor  = "#505050";
//	@Column(columnDefinition="TEXT default '#040404'")
	public String mainColor = "#111111";
//	@Column(columnDefinition="TEXT default 'Lato'")
	public String primaryFont = "Lato";
//	@Column(columnDefinition="TEXT default 'PT Serif'")
	public String secondaryFont = "PT Serif";
	@Column(columnDefinition="Decimal(10,2) default '4.0'")
	public Double titleFontSize = 4.0;
	@Column(columnDefinition="Decimal(10,2) default '1.0'")
	public Double newsFontSize = 1.0;
	
	@NotNull
	@Column(unique = true)
	public String subdomain;
	
	public boolean configured;
	
	@OneToOne
	public Image logo;
	public Integer logoId;
	public Integer logoSmallId;
	
	@OneToOne
	public Image favicon;
	public Integer faviconId;
	
	@OneToOne
	public Image splashImage;
	public Integer splashImageId;
	
	@OneToOne
	public Image loginImage;
	public Integer loginImageId;
	public Integer loginImageSmallId;
	
	@Column(columnDefinition = "varchar(255) default 'D'", nullable = false)
	public String defaultReadMode;
	@Column(columnDefinition = "varchar(255) default 'V'", nullable = false)
	public String defaultOrientationMode;

	@Override
	public String toString() {
		return "Network [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			try {
				Network network = (Network) obj;
				return Objects.equals(id, network.id) && Objects.equals(name, network.name);
			} catch (ClassCastException e) {}
		}
		return false;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		if(defaultReadMode == null || defaultReadMode.isEmpty())
			defaultReadMode = "D";
		if(defaultOrientationMode == null || defaultOrientationMode.isEmpty())
			defaultOrientationMode = "H";
		
		if(logo != null && logo.original != null){
			logoId = logo.original.id;
			logoSmallId = logo.small.id;
		}else{
			logoId = null;
			logoSmallId = null;
		}

		if(favicon != null && favicon.original != null){
			faviconId = favicon.original.id;
		}else{
			faviconId = null;
		}

		if(splashImage != null && splashImage.original != null){
			splashImageId = splashImage.original.id;
		}else{
			splashImageId = null;
		}

		if(loginImage != null && loginImage.original != null){
			loginImageId = loginImage.original.id;
			loginImageSmallId = loginImage.small.id;
		}else{
			loginImageId = null;
			loginImageSmallId = null;
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
			logoSmallId = logo.small.id;
		}else{
			logoId = null;
			logoSmallId = null;
		}

		if(favicon != null && favicon.original != null){
			faviconId = favicon.original.id;
		}else{
			faviconId = null;
		}

		if(splashImage != null && splashImage.original != null){
			splashImageId = splashImage.original.id;
		}else{
			splashImageId = null;
		}

		if(loginImage != null && loginImage.original != null){
			loginImageId = loginImage.original.id;
			loginImageSmallId = loginImage.small.id;
		}else{
			loginImageId = null;
			loginImageSmallId = null;
		}
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}