package co.xarx.trix.persistence.repository;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.domain.page.query.statement.PersonStatement;
import co.xarx.trix.jooq.tables.Person;
import co.xarx.trix.jooq.tables.records.PersonRecord;
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

	public List<PersonData> findAll(PersonStatement params, int pageSize, int pageNumber, Sort sort) {
		Person person = PERSON.as("person");

		SelectJoinStep select = dsl.select(PERSON.ID,
				PERSON.NAME,
				PERSON.USERNAME,
				PERSON.EMAIL,
				PERSON.TWITTERHANDLE.as("twitter"),
				PERSON.COVERHASH,
				PERSON.IMAGEHASH.as("profilePictureHash"))
				.from(person);

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

		Condition emailCondition = selectConditions(PERSON.EMAIL, params.getEmails());
		Condition usernameConditions = selectConditions(PERSON.USERNAME, params.getUsernames());

		SelectConditionStep selectWithWhere = insertConditions(select, emailCondition, usernameConditions);

		ResultQuery record;
		if (selectWithWhere != null) {
			record = selectWithWhere.limit(pageSize).offset(pageNumber);
		} else {
			record = select.limit(pageSize).offset(pageNumber);
		}

		log.info(record.getSQL());

		return record.fetchInto(PersonData.class);
	}

	private SelectConditionStep insertConditions(SelectJoinStep select, Condition... conditions) {
		SelectConditionStep conditionStep = null;
		for (Condition c : conditions) {
			if (c != null && conditionStep == null)
				conditionStep = select.where(c);
			else if (c != null)
				conditionStep = conditionStep.or(c);
		}

		return conditionStep;
	}

	private <T> Condition selectConditions(TableField<PersonRecord, T> field, List<T> fields) {
		Condition condition = null;
		if(fields != null && !fields.isEmpty()) {
			for (T f : fields) {
				if (condition == null)
					condition = field.eq(f);
				else
					condition = condition.or(field.eq(f));
			}
		}

		return condition;
	}
}
