package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCategory {

    public Integer id;
    public String name;

    public EventCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public EventCategory() {
    }
}
