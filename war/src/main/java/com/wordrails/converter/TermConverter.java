package com.wordrails.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.TermView;
import com.wordrails.domain.Term;
import com.wordrails.persistence.TermRepository;

@Component
public class TermConverter extends AbstractConverter<Term, TermView> {

	@Autowired TermRepository termRepository;
	
	@Override
	public Term convertToEntity(TermView termView) {
		return termRepository.findOne(termView.termId);
	}

	@Override
	public TermView convertToView(Term term) {
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