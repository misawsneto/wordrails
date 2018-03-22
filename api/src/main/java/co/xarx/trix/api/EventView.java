package co.xarx.trix.api;

import java.util.List;

public class EventView extends PostView {

    private String excerpt;
    private String moreInfo;
    private String eventLink;

    private boolean showMap;
    private String cost;
    private String color;

    private EventDate start;
    private EventDate end;

    private List<EventSchedule> schedule;
    private Organizer mainOrganizer;
    private List<Organizer> additionalOrganizers;
    private EventAddress address;
}