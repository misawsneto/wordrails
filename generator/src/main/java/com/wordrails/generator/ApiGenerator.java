package com.wordrails.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

class ApiGenerator {
	private static final String ENCODING = "UTF-8";

	private File directory;
	private String apiPackage;
	private PersistenceUnitDescription unit;
	private STGroupDir templates;		
	
	private ApiGenerator(String directory, String apiPackage, String unitPackage) {
		this.directory = new File(directory);
		this.apiPackage = apiPackage;
		this.unit = new PersistenceUnitDescription(unitPackage);
		this.templates = new STGroupDir("com/wordrails/generator/java/api", ENCODING, '$', '$');
	}

	private void generate() throws IOException {
		directory.mkdirs();
		
		generateLink();
		generateEntityDto();
		generateDtos();
		generateProjections();
		generateContentResponse();
	}

	private void generateLink() throws IOException {
		ST template = templates.getInstanceOf("Link");
		template.add("package", apiPackage);
		template.write(new File(directory, "Link.java"), null, ENCODING);		
	}
	
	private void generateEntityDto() throws IOException {
		ST template = templates.getInstanceOf("EntityDto");
		template.add("package", apiPackage);
		template.write(new File(directory, "EntityDto.java"), null, ENCODING);		
	}

	private void generateDtos() throws IOException {
		for (EntityDescription entity : unit.entities) {
			generateDto(entity);
		}		
	}

	private void generateDto(EntityDescription entity) throws IOException {
		ST template = templates.getInstanceOf("Dto");
		template.add("package", apiPackage);
		template.add("entity", entity);
		template.write(new File(directory, entity.name + "Dto.java"), null, ENCODING);
	}

	private void generateProjections() throws IOException {
		Set<EntityDescription> projections = new HashSet<>();
		for (EntityDescription entity : unit.entities) {
			for (EntityDescription projection : entity.projections) {
				projections.add(projection);
			}
		}
		for (EntityDescription projection : projections) {
			generateProjectionDto(projection);
		}
	}	

	private void generateProjectionDto(EntityDescription entity) throws IOException {
		ST template = templates.getInstanceOf("ProjectionDto");
		template.add("package", apiPackage);
		template.add("entity", entity);
		template.write(new File(directory, entity.name + "Dto.java"), null, ENCODING);
	}
	
	private void generateContentResponse() throws IOException {
		ST template = templates.getInstanceOf("ContentResponse");
		template.add("package", apiPackage);
		template.write(new File(directory, "ContentResponse.java"), null, ENCODING);		
	}

	public static void main(String[] args) throws IOException {
		ApiGenerator generator = new ApiGenerator(args[0] + "/com/wordrails/api", "com.wordrails.api", "com.wordrails");
		generator.generate();
	}	
}