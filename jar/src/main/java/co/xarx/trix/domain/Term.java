package co.xarx.trix.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames={"taxonomy_id","name"})
})
public class Term extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7891255759575029731L;
	
	@Size(min=1, max=100)
	public String name;

	@JsonBackReference("cells")
	@OneToMany(mappedBy="term")
	public Set<Cell> cells;

	@JsonBackReference("posts")
	@ManyToMany(mappedBy="terms")
	public Set<Post> posts;

	@JsonBackReference("rows")
	@OneToMany(mappedBy="term")
	public Set<Row> rows;
	
	@NotNull
	@ManyToOne
	public Taxonomy taxonomy;

	@JsonBackReference("parent")
	@ManyToOne
	public Term parent;

	@OneToMany(mappedBy="parent")
	public Set<Term> children;

	@JsonBackReference("termPerspectives")
	@OneToMany(mappedBy="term")
	public Set<TermPerspective> termPerspectives;

	public Integer taxonomyId;
	public String taxonomyName;

	@PrePersist
	void onCreate(){
		if(taxonomy != null && taxonomy.id != null) {
			taxonomyId = taxonomy.id;
			taxonomyName = taxonomy.name;
		}
	}
	
	@PreUpdate
	void onUpdate(){
		if(taxonomy != null && taxonomy.id != null) {
			taxonomyId = taxonomy.id;
			taxonomyName = taxonomy.name;
		}
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