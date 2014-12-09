package com.wordrails.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Component;

@Path("/search")
@Consumes(MediaType.WILDCARD)
@Component
public class SearchManagementResource {
	private @PersistenceContext EntityManager manager;

	/**
	 * Method to manually update the Full Text Index. This is not required if inserting entities
	 * using this Manager as they will automatically be indexed. Useful though if you need to index
	 * data inserted using a different method (e.g. pre-existing data, or test data inserted via
	 * scripts or DbUnit).
	 */
	@GET
	@Path("/update")
	public Response updateFullTextIndex(@Context HttpServletRequest request) throws Exception {

		FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
		ftem.createIndexer().startAndWait();
		ftem.flushToIndexes();
		return Response.status(Status.OK).entity("Updating...").build();
	}

	/**
	 * Regenerates all the indexed class indexes
	 *
	 * @param async true if the reindexing will be done as a background thread
	 * @param sess the hibernate session
	 */
	@GET
	@Path("/reindex")
	public Response reindexAll(@Context HttpServletRequest request) {

		FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
		MassIndexer massIndexer = ftem.createIndexer();
		massIndexer.purgeAllOnStart(true);
//		massIndexer.start;
		try {
			massIndexer.startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ftem.flushToIndexes();

		return Response.status(Status.OK).entity("Reindexing...").build();
	}
}
