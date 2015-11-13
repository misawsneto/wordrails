package co.xarx.trix.generator;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import java.io.File;
import java.io.IOException;

class ImplementationGenerator {
	private static final String ENCODING = "UTF-8";

	private File directory;
	private String apiPackage;
	private String facade;
	private PersistenceUnitDescription unit;
	private STGroupDir templates;		

	private ImplementationGenerator(String directory, String apiPackage, String facade, String unitPackage) {
		this.directory = new File(directory);
		this.apiPackage = apiPackage;
		this.facade = facade;
		this.unit = new PersistenceUnitDescription(unitPackage);
		this.templates = new STGroupDir("co/xarx/trix/generator/java/implementation/filter", ENCODING, '$', '$');
	}

	private void generate() throws IOException {
		directory.mkdirs();

		generatePATCH();
		generateFilter();
//		generateFacade();
//		generateBatch();
	}

	private void generatePATCH() throws IOException {
		ST template = templates.getInstanceOf("PATCH");
		template.add("package", apiPackage);
		template.write(new File(directory, "PATCH.java"), null, ENCODING);
	}
	
	private void generateFilter() throws IOException {
		ST template = templates.getInstanceOf("Facade");
		template.add("package", apiPackage);
		template.add("entities", unit.entities);
		template.write(new File(directory, "AbstractAuthorizationFilter.java"), null, ENCODING);
	}
	
	private void generateFacade() throws IOException {
		ST template = templates.getInstanceOf("Facade");
		template.add("package", apiPackage);
		template.add("facade", facade);
		template.add("entities", unit.entities);
		template.write(new File(directory, facade + ".java"), null, ENCODING);
	}

	private void generateBatch() throws IOException {
		ST template = templates.getInstanceOf("Batch");
		template.add("package", apiPackage);
		template.add("facade", facade);
		template.add("entities", unit.entities);
		template.write(new File(directory, "Batch.java"), null, ENCODING);
	}

	public static void main(String[] args) throws IOException {
		ImplementationGenerator generator = new ImplementationGenerator(args[0] + "/co/xarx/trix/api", "co.xarx.trix.api", "WordRailsResource", "co.xarx.trix");
		generator.generate();
	}	
}