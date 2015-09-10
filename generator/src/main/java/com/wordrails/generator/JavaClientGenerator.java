package com.wordrails.generator;

import java.io.File;
import java.io.IOException;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

class JavaClientGenerator {
	private static final String ENCODING = "UTF-8";

	private File directory;
	private String apiPackage;
	private String facade;
	private PersistenceUnitDescription unit;
	private STGroupDir templates;		
	
	private JavaClientGenerator(String directory, String apiPackage, String facade, String unitPackage) {
		this.directory = new File(directory);
		this.apiPackage = apiPackage;
		this.facade = facade;
		this.unit = new PersistenceUnitDescription(unitPackage);
		this.templates = new STGroupDir("com/wordrails/generator/java/client", ENCODING, '$', '$');
	}
	
	private void generate() throws IOException {
		directory.mkdirs();
				
		generateIFacade();
		generateIConnectivityManager();
		generateConnectivityManagerImpl();
		generateMockConnectivityManager();
		generateAuthenticationRequestInterceptor();		
		generateAuthenticationInvocationHandler();
		generateDateJsonSerializer();
		generateDateJsonDeserializer();
		generateFacade();
		generateBatch();
	}

	private void generateIFacade() throws IOException {
		ST template = templates.getInstanceOf("IFacade");
		template.add("package", apiPackage);
		template.add("facade", facade);
		template.add("entities", unit.entities);
		template.write(new File(directory, "I" + facade + ".java"), null, ENCODING);		
	}

	private void generateIConnectivityManager() throws IOException {
		ST template = templates.getInstanceOf("IConnectivityManager");
		template.add("package", apiPackage);
		template.write(new File(directory, "IConnectivityManager.java"), null, ENCODING);		
	}	
	
	private void generateConnectivityManagerImpl() throws IOException {
		ST template = templates.getInstanceOf("ConnectivityManagerImpl");
		template.add("package", apiPackage);
		template.write(new File(directory, "ConnectivityManagerImpl.java"), null, ENCODING);		
	}
	
	private void generateMockConnectivityManager() throws IOException {
		ST template = templates.getInstanceOf("MockConnectivityManager");
		template.add("package", apiPackage);
		template.write(new File(directory, "MockConnectivityManager.java"), null, ENCODING);		
	}

	private void generateAuthenticationRequestInterceptor() throws IOException {
		ST template = templates.getInstanceOf("AuthenticationRequestInterceptor");
		template.add("package", apiPackage);
		template.write(new File(directory, "AuthenticationRequestInterceptor.java"), null, ENCODING);		
	}	
	
	private void generateAuthenticationInvocationHandler() throws IOException {
		ST template = templates.getInstanceOf("AuthenticationInvocationHandler");
		template.add("package", apiPackage);
		template.write(new File(directory, "AuthenticationInvocationHandler.java"), null, ENCODING);		
	}
			
	private void generateDateJsonSerializer() throws IOException {
		ST template = templates.getInstanceOf("DateJsonSerializer");
		template.add("package", apiPackage);
		template.write(new File(directory, "DateJsonSerializer.java"), null, ENCODING);		
	}

	private void generateDateJsonDeserializer() throws IOException {
		ST template = templates.getInstanceOf("DateJsonDeserializer");
		template.add("package", apiPackage);
		template.write(new File(directory, "DateJsonDeserializer.java"), null, ENCODING);		
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
		JavaClientGenerator generator = new JavaClientGenerator(args[0] + "/com/wordrails/api", "com.wordrails.api", "WordRails", "com.wordrails");
		generator.generate();
	}		
}