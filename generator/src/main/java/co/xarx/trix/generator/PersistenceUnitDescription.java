package co.xarx.trix.generator;

import co.xarx.trix.generator.exception.InvalidEntityException;
import co.xarx.trix.generator.exception.InvalidProjectionException;
import co.xarx.trix.generator.scope.EntityDescription;
import co.xarx.trix.generator.scope.QueryDescription;
import org.reflections.Reflections;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersistenceUnitDescription {

	private String packageToScan;

	private List<EntityDescription> entities;

	private Map<Class<?>, List<QueryDescription>> queries;
	private Map<Class<?>, Class<?>> repositories;
	private Map<Class<?>, EntityDescription> projections;

	public PersistenceUnitDescription(String packageToScan) throws InvalidProjectionException, InvalidEntityException {
		this.packageToScan = packageToScan;

		Reflections reflections = new Reflections(packageToScan);
		Set<Class<?>> projectionClasses = reflections.getTypesAnnotatedWith(Projection.class);
		ProjectionExtractor projectionExtractor = new ProjectionExtractor(projectionClasses);

		Set repositoryClasses = reflections.getTypesAnnotatedWith(RepositoryRestResource.class);
		RepositoryExtractor repositoryExtractor = new RepositoryExtractor(repositoryClasses);

		queries = repositoryExtractor.getRepositoryQueries();
		repositories = repositoryExtractor.getRepositoryEntities();
		projections = projectionExtractor.getProjections();

		entities = getEntityDescriptions();
	}

	public List<EntityDescription> getEntities() {
		return entities;
	}

	private List<EntityDescription> getEntityDescriptions() throws InvalidEntityException {
		EntityExtractor entityExtractor = new EntityExtractor();
		Set<Class<?>> entityClasses = entityExtractor.getEntityClasses(packageToScan);
		Map<Class<?>, EntityDescription> entityMap = entityExtractor.extractEntities(entityClasses, queries,
				repositories, projections);

		List<EntityDescription> entities = new ArrayList<>();
		entities.addAll(entityMap.values());
		entityExtractor.sortEntitiesByName(entities);
		entityExtractor.insertEntitiesIntoFields(entities);

		return entities;
	}
}