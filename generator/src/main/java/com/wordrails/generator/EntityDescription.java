package com.wordrails.generator;

import java.util.ArrayList;
import java.util.List;

public class EntityDescription {
	public String fullName;
	public String name;
	public String nameLowercase;
	public String plural;
	public String pluralLowercase;
	public FieldDescription id;	
	public List<FieldDescription> fields = new ArrayList<FieldDescription>();
	public List<FieldDescription> relationships = new ArrayList<FieldDescription>();
	public String repositoryFullName;
	public String repositoryName;	
	public List<QueryDescription> queries = new ArrayList<QueryDescription>();
	public List<EntityDescription> projections = new ArrayList<EntityDescription>();
}