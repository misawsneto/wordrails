package co.xarx.trix.services.analytics;

import co.xarx.trix.domain.ESstatEvent;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.ESstatEventRepository;
import co.xarx.trix.persistence.TermRepository;
import org.jcodec.common.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TermsImporter {
    private ESstatEventRepository statEventRepository;
    private TermRepository termRepository;
    private boolean importTerms;

    @Autowired
    public TermsImporter(ESstatEventRepository statEventRepository, TermRepository termRepository, @Value("${elasticsearch.termsImport}") boolean importTerms){
        this.statEventRepository = statEventRepository;
        this.termRepository = termRepository;
        this.importTerms = importTerms;
    }


    @PostConstruct
    public void importTermsToStats(){
        if(!importTerms) return;

        Logger.info("Importing terms to stat events");

        Iterable<ESstatEvent> events = statEventRepository.findAll();
        for(ESstatEvent e: events){
            updateEvent(e);
            statEventRepository.save(e);
        }
    }

    private void updateEvent(ESstatEvent event){
        if(event.getPostId() != null){
            List<Term> terms = termRepository.findTermsByPostId(event.getPostId());
            event.setTerms(terms.stream().map(Term::getId).collect(Collectors.toList()));
        }
    }
}
