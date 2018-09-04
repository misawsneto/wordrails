package co.xarx.trix.api;

import java.util.List;

public class MenuItemView {

    public Integer id;
    public Integer postId;
    public Integer stationId;
    public Integer parent;
    public String title;
    public String icon;
    public String url;
    public String object;
    public String slug;
    public Integer menuOrder;

    public List<MenuItemView> children;
}
