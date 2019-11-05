package org.app.mbooster.model_folder;

/**
 * Created by royfei on 02/11/2017.
 */

public class NotificationTitleItem {
    public String type;
    public String title, notificationcount;
    public String icon;
    public String link_url;

    public NotificationTitleItem(String type,String title, String icon, String notificationcount, String link_url ) {
        this.type = type;
        this.title = title;
        this.icon = icon;
        this.notificationcount = notificationcount;
        this.link_url  = link_url;
    }
}
