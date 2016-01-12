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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Cell> getCells() {
		return cells;
	}

	public void setCells(Set<Cell> cells) {
		this.cells = cells;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<Row> getRows() {
		return rows;
	}

	public void setRows(Set<Row> rows) {
		this.rows = rows;
	}

	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}

	public Term getParent() {
		return parent;
	}

	public void setParent(Term parent) {
		this.parent = parent;
	}

	public Set<Term> getChildren() {
		return children;
	}

	public void setChildren(Set<Term> children) {
		this.children = children;
	}

	public Set<TermPerspective> getTermPerspectives() {
		return termPerspectives;
	}

	public void setTermPerspectives(Set<TermPerspective> termPerspectives) {
		this.termPerspectives = termPerspectives;
	}

	public Integer getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

	public String getTaxonomyName() {
		return taxonomyName;
	}

	public void setTaxonomyName(String taxonomyName) {
		this.taxonomyName = taxonomyName;
	}
}