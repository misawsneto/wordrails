package com.wordrails.generator;

import java.io.File;
import java.io.IOException;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

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
		this.templates = new STGroupDir("com/wordrails/generator/angular", ENCODING, '$', '$');
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

	public static void main(String[] args) throws IOException {
		//"C:/Users/misael/PROJECTs/WORDRAILS2_TRUNK/wordrails2/war/src/main/webapp/js"
//		AngularClientGenerator generator = new AngularClientGenerator("C:/Users/misael/PROJECTs/WORDRAILS2_TRUNK/wordrails2/war/src/main/webapp/js", "Trix", "com.wordrails"); 
//		generator.generate();
		AngularClientGenerator generator = new AngularClientGenerator(args[0], "Trix", "com.wordrails"); 
		generator.generate();
	}		
}