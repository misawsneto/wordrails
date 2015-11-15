package co.xarx.trix.generator;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import java.io.File;
import java.io.IOException;

class JavaScriptClientGenerator {
	private static final String ENCODING = "UTF-8";

	private File directory;
	private String facade;
	private PersistenceUnitDescription unit;
	private STGroupDir templates;		
	
	private JavaScriptClientGenerator(String directory, String facade, String unitPackage) {
		this.directory = new File(directory);
		this.facade = facade;
		this.unit = new PersistenceUnitDescription(unitPackage);
		this.templates = new STGroupDir("co/xarx/trix/generator/javascript", ENCODING, '$', '$');
	}
	
	private void generate() throws IOException {
		directory.mkdirs();
		
		generateJavaScript();
	}

	public void generateJavaScript() throws IOException {
		ST template = templates.getInstanceOf("Facade");
		template.add("facade", facade);
		template.add("entities", unit.entities);
		template.write(new File(directory, "Base" + facade + ".js"), null, ENCODING);
	}

	public static void main(String[] args) throws IOException {
		JavaScriptClientGenerator generator = new JavaScriptClientGenerator(args[0], "WordRails", "co.xarx.trix");
		generator.generate();
	}		
}