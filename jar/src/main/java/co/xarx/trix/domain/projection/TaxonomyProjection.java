package co.xarx.trix.domain.projection;

import java.util.Set;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.Term;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=Taxonomy.class)
public interface TaxonomyProjection {
	Integer getId();
	
	String getType();
	String getName();
	Set<Network> getNetworks();
	Set<Term> terms();
	Network owningNetwork();
	Station owningStation();
}