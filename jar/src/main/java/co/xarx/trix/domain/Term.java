package co.xarx.trix.domain;


import co.xarx.trix.annotation.SdkInclude;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Setter;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames={"taxonomy_id","name_parent"})
})
public class Term extends BaseEntity implements Serializable, Comparable<Term>{
	private static final long serialVersionUID = 7891255759575029731L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Size(min=1, max=100)
	public String name;

	public String name_parent;

	@DiffIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Taxonomy taxonomy;

	@JsonBackReference("parent")
	@ManyToOne
	public Term parent;

	@DiffIgnore
	@OneToMany(mappedBy="parent")
	public Set<Term> children;

	@DiffIgnore
	@JsonBackReference("termPerspectives")
	@OneToMany(mappedBy="term")
	public Set<TermPerspective> termPerspectives;

	public Integer taxonomyId;
	public String taxonomyName;

	public String color;

	@ManyToOne(cascade = CascadeType.ALL)
	public Image image;

	@Lob
	public String description;

	public String getTermName(){
		if(name != null)
			return name;
		return null;
	}

	public Integer getTermID(){
		if(id != null)
			return id;
		return null;
	}

	@SdkInclude
	public String getImageHash() {
		if (image != null) return image.getOriginalHash();

		return null;
	}

	@PrePersist
	void onCreate(){
		if(taxonomy != null && taxonomy.id != null) {
			taxonomyId = taxonomy.id;
			taxonomyName = taxonomy.name;
		}

		name_parent = name + "_" + (parent != null ? parent.id : "0");
	}
	
	@PreUpdate
	void onUpdate(){
		if(taxonomy != null && taxonomy.id != null) {
			taxonomyId = taxonomy.id;
			taxonomyName = taxonomy.name;
		}

		name_parent = name + "_" + (parent != null ? parent.id : "0");
	}

	@Override
	public boolean equals(Object obj) {
		if(id != null)
			return this.id.equals(((Term)obj).id);
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if(id != null)
			return id.hashCode() * 31;
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "Term{" +
				"name='" + name + '\'' +
				'}';
	}

	@Override
	public int compareTo(Term o) {
		if(this.id != null &&  o.id != null)
			return Integer.compare(this.id, o.id);
		else if(this.id == null && o.id != null)
			return 1;
		else if(this.id != null && o.id == null)
			return -1;
		else if(this.id == null && o.id == null)
			return 1;

		else return 0;
	}
}