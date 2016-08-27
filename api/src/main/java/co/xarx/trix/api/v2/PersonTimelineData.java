package co.xarx.trix.api.v2;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PersonTimelineData {
    public List<PersonEventData> events;
}
