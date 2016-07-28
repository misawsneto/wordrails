package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
public class Taxonomy extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7821255752575024731L;

	public static final String NETWORK_TAXONOMY = "N";
	public static final String STATION_TAXONOMY = "S";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@NotNull
	@Size(min=1, max=1)
	@Column(updatable = false)
	public String type;

	@Size(min=1, max=100)
	public String name;

//	@ManyToMany(mappedBy="taxonomies")
//	public Set<Network> networks;

	@OneToMany(mappedBy="taxonomy")
	public Set<Term> terms;


	@ManyToOne
	@JoinColumn(updatable=true)
	public Network owningNetwork;

	@ManyToOne
	@JoinColumn(updatable=true)
	public Station owningStation;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owningNetwork == null) ? 0 : owningNetwork.hashCode());
		result = prime * result + ((owningStation == null) ? 0 : owningStation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Taxonomy [id=" + id + ", type=" + type + ", name=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Taxonomy other = (Taxonomy) obj;
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
		if (owningNetwork == null) {
			if (other.owningNetwork != null)
				return false;
		} else if (!owningNetwork.equals(other.owningNetwork))
			return false;
		if (owningStation == null) {
			if (other.owningStation != null)
				return false;
		} else if (!owningStation.equals(other.owningStation))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}