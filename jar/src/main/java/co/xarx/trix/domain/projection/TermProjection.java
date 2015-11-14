package co.xarx.trix.domain.projection;

import java.util.Set;

import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.Term;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=Term.class)
public interface TermProjection {

	Integer getId();
	String getName();
	Taxonomy getTaxonomy();
	Term getParent();
	Set<Term> getChildren();
	String getColor();
}
