package co.xarx.trix.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.xarx.trix.api.TermView;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.TermRepository;

import java.util.ArrayList;
import java.util.List;

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
		termView.termId = termView.id = term.id;
		if(term.parent != null)
			termView.parentId = term.parent.id;
		if(term.taxonomy != null)
			termView.taxonomyId = term.taxonomy.id;
		termView.termName = term.name;
		termView.name = term.name;
		termView.color = term.color;
		termView.description = term.description;
		termView.imageHash = term.image != null ? term.image.getOriginalHash() : null;
		
		return termView;
	}

	public TermView convertToTermTree(Term term) {
		TermView view = convertTo(term);
		if(term.children != null)
			view.children = convertToViewsTree(new ArrayList<>(term.children));
		return view;
	}

	public List<TermView> convertToViewsTree(List<Term> terms) {
		List<TermView> views = new ArrayList<TermView>(terms.size());
		for (Term term : terms) {
			TermView view = convertToTermTree(term);
			if(view!=null)
				views.add(view);
		}
		return views;
	}
}