package co.xarx.trix.domain;


import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Term extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 7891255759575029731L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Size(min=1, max=100)
	public String name;

	public String name_parent;

	@DiffIgnore
	@JsonBackReference("cells")
	@OneToMany(mappedBy="term")
	public Set<Cell> cells;

	@DiffIgnore
	@JsonBackReference("posts")
	@ManyToMany(mappedBy="terms")
	public Set<Post> posts;

	@DiffIgnore
	@JsonBackReference("rows")
	@OneToMany(mappedBy="term")
	public Set<Row> rows;

	@DiffIgnore
	@NotNull
	@ManyToOne
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

	@SdkExclude
	@ManyToOne(cascade=CascadeType.MERGE)
	@JsonIgnore
	private Picture image;

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
}