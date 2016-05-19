package co.xarx.trix.persistence.repository;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.jooq.tables.Person;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static co.xarx.trix.jooq.tables.Person.PERSON;

@Slf4j
@Repository
public class SQLPersonRepository {

	private DSLContext dsl;

	@Autowired
	public SQLPersonRepository(DSLContext dslContext) {
		this.dsl = dslContext;
	}

	public List<PersonData> findAll(int pageSize, int pageNumber, Sort sort) {
		Person person = PERSON.as("person");

		SelectJoinStep select = dsl.select(PERSON.ID, PERSON.NAME, PERSON.USERNAME).from(person);

		while (sort != null && sort.iterator().hasNext()) {
			Sort.Order order = sort.iterator().next();
			Field field = PERSON.field(order.getProperty());
			SortField sortField;
			if(order.getDirection() == Sort.Direction.ASC) {
				sortField = field.asc();
			} else {
				sortField = field.desc();
			}
			select.orderBy(sortField);
		}


		ResultQuery record = select
				.limit(pageSize)
				.offset(pageNumber);

		log.info(record.getSQL());

		return record.fetchInto(PersonData.class);
	}
}
