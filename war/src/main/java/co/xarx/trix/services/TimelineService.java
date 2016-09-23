package co.xarx.trix.services;

import co.xarx.trix.domain.ESstatEvent;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.ESstatEventRepository;
import co.xarx.trix.persistence.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class TimelineService {
    private PersonRepository personRepository;
    private ESstatEventRepository eSstatEventRepository;

    @Autowired
    public TimelineService(PersonRepository personRepository, ESstatEventRepository eSstatEventRepository){
        this.eSstatEventRepository = eSstatEventRepository;
        this.personRepository = personRepository;
    }

    public List<ESstatEvent> getUserTimeline(String username) {
        Person person = personRepository.findByUsername(username);
        Assert.notNull(person, "Person not found for username: " + username);

        return eSstatEventRepository.findByPersonId(person.getId());
    }
}
