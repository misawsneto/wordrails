package co.xarx.trix.generator.generator;

import co.xarx.trix.generator.PersistenceUnitDescription;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import java.io.File;
import java.io.IOException;

class AngularClientGenerator {
	private static final String ENCODING = "UTF-8";

	private File directory;
	private String facade;
	private PersistenceUnitDescription unit;
	private STGroupDir templates;

	private AngularClientGenerator(String directory, String facade, String unitPackage) {
		this.directory = new File(directory);
		this.facade = facade;
		this.unit = new PersistenceUnitDescription(unitPackage);
		this.templates = new STGroupDir("co/xarx/trix/generator/angular", ENCODING, '$', '$');
	}

	public static void main(String[] args) throws IOException {
		AngularClientGenerator generator = new AngularClientGenerator(args[0], "Trix", "co.xarx.trix");
		generator.generate();
	}

	private void generate() throws IOException {
		directory.mkdirs();

		generateAngular();
	}

	public void generateAngular() throws IOException {
		ST template = templates.getInstanceOf("Facade");
		template.add("facade", facade);
		template.add("entities", unit.entities);
		template.write(new File(directory, "Base" + facade + ".js"), null, ENCODING);
	}
}