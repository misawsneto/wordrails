package co.xarx.trix.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.xarx.trix.api.TermView;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.TermRepository;

@Component
public class TermConverter extends AbstractConverter<Term, TermView> {

	@Autowired TermRepository termRepository;
	
	@Override
	public Term convertFrom(TermView termView) {
		return termRepository.findOne(termView.termId);
	}

	@Override
	public TermView convertTo(Term term) {
		TermView termView = new TermView();
		termView.termId = term.id;
		if(term.parent != null)
			termView.parentId = term.parent.id;
		if(term.taxonomy != null)
			termView.taxonomyId = term.taxonomy.id;
		termView.termName = term.name;
		
		return termView;
	}
}