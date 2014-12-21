package com.wordrails.business;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Network {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@Size(min=1, max=100)
	public String name;
	
	@Size(min=1, max=100)	
	public String trackingId;
	
	@OneToMany(mappedBy="network")
	public Set<NetworkRole> personsNetworkRoles;
	
	@ManyToMany(mappedBy="networks")
	public Set<Station> stations;

	@ManyToMany
	public Set<Taxonomy> taxonomies;
	
	@OneToMany(mappedBy="network", cascade=CascadeType.ALL)
	public Set<Sponsor> sponsors;
		
	@ManyToOne
	@NotNull
	public Taxonomy defaultTaxonomy;
	
	@OneToMany(mappedBy="owningNetwork")
	public Set<Taxonomy> ownedTaxonomies;
	
	public boolean allowSignup;
	
	public boolean allowComments;
	
	public String domain;
	
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public Set<NetworkRole> getPersonsNetworkRoles() {
		return personsNetworkRoles;
	}

	public void setPersonsNetworkRoles(Set<NetworkRole> personsNetworkRoles) {
		this.personsNetworkRoles = personsNetworkRoles;
	}

	public Set<Station> getStations() {
		return stations;
	}

	public void setStations(Set<Station> stations) {
		this.stations = stations;
	}

	public Set<Taxonomy> getTaxonomies() {
		return taxonomies;
	}

	public void setTaxonomies(Set<Taxonomy> taxonomies) {
		this.taxonomies = taxonomies;
	}

	public Taxonomy getDefaultTaxonomy() {
		return defaultTaxonomy;
	}

	public void setDefaultTaxonomy(Taxonomy defaultTaxonomy) {
		this.defaultTaxonomy = defaultTaxonomy;
	}

	public Set<Taxonomy> getOwnedTaxonomies() {
		return ownedTaxonomies;
	}

	public void setOwnedTaxonomies(Set<Taxonomy> ownedTaxonomies) {
		this.ownedTaxonomies = ownedTaxonomies;
	}

	public boolean isAllowSignup() {
		return allowSignup;
	}

	public void setAllowSignup(boolean allowSignup) {
		this.allowSignup = allowSignup;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(boolean allowComments) {
		this.allowComments = allowComments;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}

	public Set<Sponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(Set<Sponsor> sponsors) {
		this.sponsors = sponsors;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getNavbarColor() {
		return navbarColor;
	}

	public void setNavbarColor(String navbarColor) {
		this.navbarColor = navbarColor;
	}

	public String getNavbarSecondaryColor() {
		return navbarSecondaryColor;
	}

	public void setNavbarSecondaryColor(String navbarSecondaryColor) {
		this.navbarSecondaryColor = navbarSecondaryColor;
	}

	public String getMainColor() {
		return mainColor;
	}

	public void setMainColor(String mainColor) {
		this.mainColor = mainColor;
	}

	public String getPrimaryFont() {
		return primaryFont;
	}

	public void setPrimaryFont(String primaryFont) {
		this.primaryFont = primaryFont;
	}

	public String getSecondaryFont() {
		return secondaryFont;
	}

	public void setSecondaryFont(String secondaryFont) {
		this.secondaryFont = secondaryFont;
	}

	public Double getTitleFontSize() {
		return titleFontSize;
	}

	public void setTitleFontSize(Double titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	public Double getNewsFontSize() {
		return newsFontSize;
	}

	public void setNewsFontSize(Double newsFontSize) {
		this.newsFontSize = newsFontSize;
	}

}