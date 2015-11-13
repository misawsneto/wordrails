package co.xarx.trix.generator;

import java.util.ArrayList;
import java.util.List;

public class QueryDescription {
	public String name;
	public String nameUppercase;
	public boolean collection;
	public List<FieldDescription> parameters = new ArrayList<>();	
}