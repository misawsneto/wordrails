package co.xarx.trix.services;

import co.xarx.trix.api.v2.PersonEventData;
import co.xarx.trix.api.v2.PersonTimelineData;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Service
public class TimelineService {
    private Client client;
    private DateTimeFormatter fmt;
    private String nginxAccessIndex;
    private PersonRepository personRepository;

    @Autowired
    public TimelineService(Client client, @Value("${elasticsearch.nginxAccessIndex}") String nginxAccessIndex, PersonRepository personRepository){
        this.client = client;
        this.nginxAccessIndex = nginxAccessIndex;
        this.personRepository = personRepository;
        fmt = ISODateTimeFormat.dateTime();
    }

    public PersonTimelineData getUserTimeline(String username) {
        Person person = personRepository.findByUsername(username);
        Assert.notNull(person, "Person not found: personId " + username);

        SearchResponse response = getTimelineQuery(username).execute().actionGet();
        PersonTimelineData timeline = extractResult(response);

        return timeline;
    }

    private SearchRequestBuilder getTimelineQuery(String username){
        SearchRequestBuilder query = client.prepareSearch(nginxAccessIndex);
        query.setQuery(boolQuery().must(termQuery("username", username)));
        query.setQuery(boolQuery().must(termQuery("response", 200)));
        query.addFields("request", "@timestamp");

        return query;
    }

    private PersonTimelineData extractResult(SearchResponse response){
        response.getHits().getHits()[0].field("@timestamp").getValue().toString();
        List<PersonEventData> events = new ArrayList<>();

        for(SearchHit hit: response.getHits().getHits()){
            String request = hit.field("request").getValue().toString();
            String timestamp = hit.field("@timestamp").getValue();
            PersonEventData event = new PersonEventData(request, toDate(timestamp));
            events.add(event);
        }

        return new PersonTimelineData(events);
    }

    private Date toDate(String date){
        return new Date(fmt.parseDateTime(date).getMillis()/1000);
    }
}
