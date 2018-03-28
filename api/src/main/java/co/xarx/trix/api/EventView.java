package co.xarx.trix.api;

import java.util.List;

public class EventView extends PostView {

    public String excerpt;
    public String moreInfo;
    public String eventLink;

    public boolean showMap;
    public String cost;
    public String color;

    public EventDate start;
    public EventDate end;

    public List<EventSchedule> schedule;
    public Organizer mainOrganizer;
    public List<Organizer> additionalOrganizers;
    public EventAddress address;
}