package com.wordrails.business;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"taxonomy_id","name"})})
public class Term {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@Size(min=1, max=100)
	@Field // field used by Post object
	public String name;
	
	@OneToMany(mappedBy="term")
	public Set<Cell> cells;
		
	@ManyToMany(mappedBy="terms")
	@ContainedIn
	public Set<Post> posts;
	
	@OneToMany(mappedBy="term")
	public Set<Row> rows;	
	
	@NotNull
	@ManyToOne	
	public Taxonomy taxonomy;

	@ManyToOne
	public Term parent;
			
	@OneToMany(mappedBy="parent")
	public Set<Term> children;
	
	@OneToMany(mappedBy="term")
	public Set<TermPerspective> termPerspectives;
}