package co.xarx.trix.generator.scope;

import java.util.ArrayList;
import java.util.List;

public class EntityDescription {
	public String fullName;
	public String name;
	public String nameLowercase;
	public String plural;
	public String pluralLowercase;
	public FieldDescription id;
	public List<FieldDescription> fields = new ArrayList<>();
	public List<FieldDescription> relationships = new ArrayList<>();
	public String repositoryFullName;
	public String repositoryName;
	public List<QueryDescription> queries = new ArrayList<>();
	public List<EntityDescription> projections = new ArrayList<>();

	public boolean findAll;
	public boolean findOne;
	public boolean save;
	public boolean delete;

	@Override
	public String toString() {
		return "EntityDescription{" +
				"fullName='" + fullName + '\'' +
				", name='" + name + '\'' +
				", nameLowercase='" + nameLowercase + '\'' +
				", plural='" + plural + '\'' +
				", pluralLowercase='" + pluralLowercase + '\'' +
				", id=" + id +
				", fields=" + fields +
				", relationships=" + relationships +
				", repositoryFullName='" + repositoryFullName + '\'' +
				", repositoryName='" + repositoryName + '\'' +
				", queries=" + queries +
				", projections=" + projections +
				'}';
	}
}