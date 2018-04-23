package co.xarx.trix.api;

import java.util.List;

public class Configs {
    public Integer stationScheduleId;
    public String  stationScheduleName;
    public boolean allowTrixAlert;
    public boolean allowEvents;
    public boolean allowCarnival;
    public boolean allowGoogleLogin;
    public boolean allowFacebooklogin;

    public String  urlCarnival;
    public Integer classifiedPageLimit;
    public List<String> classifiedCategories;
    public List<Category> eventCategories;

    public List<MenuEntryDto> sections;
}
