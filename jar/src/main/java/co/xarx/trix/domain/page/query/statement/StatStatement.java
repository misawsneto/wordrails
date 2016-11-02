package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.ESstatEvent;
import co.xarx.trix.persistence.ObjectStatementRepository;
import com.amazonaws.services.apigateway.model.BadRequestException;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

@Getter
@Setter
public class StatStatement extends AbstractStatement {

    private String field;
    private Object fieldId;
    private List<String> byFields;
    private List<String> byValues;
    private String startDate;
    private String endDate;
    private Integer size;
    private Integer page;
    private Interval interval;

    private static int WEEK_INTERVAL = 7;
    private static int MONTH_INTERVAL = 30;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC();

    public StatStatement(String field, Object fieldId, String startDate, String endDate){
        this.field = field;
        this.fieldId = fieldId;
        this.interval = getInterval(endDate, startDate);
    }

    public StatStatement(String field, List<String> byFields,
                         List<String> byValues, String startDate,
                         String endDate, Integer size, Integer page){
        this.size = size;
        this.page = page;
        this.field = field;
        this.byFields = byFields;
        this.byValues = byValues;
        this.interval = getInterval(endDate, startDate);
    }

    public Interval getInterval(DateTime endDate, Integer size) {
        if (size == null) new Interval(endDate.minusDays(MONTH_INTERVAL), endDate);
        return new Interval(endDate.minusDays(size), endDate);
    }

    public Interval getInterval(String date, Integer size) {
        return getInterval(date != null ? dateTimeFormatter.parseDateTime(date) : new DateTime(), size);
    }

    public Interval getInterval(String end, String start) throws BadRequestException {
        DateTime endDate = dateTimeFormatter.parseDateTime(end);

        if (start == null || start.isEmpty()) return new Interval(endDate.minusDays(MONTH_INTERVAL), endDate);

        DateTime startDate = dateTimeFormatter.parseDateTime(start);

        if (endDate.isAfter(startDate)) return new Interval(startDate, endDate);
        throw new BadRequestException("Wrong time range. 'end' must be a date after 'start'");
    }

    public Interval getWeeklyInterval(){
        return getInterval(interval.getEnd(), WEEK_INTERVAL);
    }

    public Interval getMonthlyInterval(){
        return getInterval(interval.getEnd(), MONTH_INTERVAL);
    }

    public String getESFieldName(){
        return field != null ? field + "Id" : null;
    }

    @Override
    public Class getType() {
        return ESstatEvent.class;
    }
}
