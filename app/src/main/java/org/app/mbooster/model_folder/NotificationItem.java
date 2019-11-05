package org.app.mbooster.model_folder;

/**
 * Created by royfei on 24/10/2017.
 */

public class NotificationItem {
    public String id;
    public String title;
    public String image_url;
    public String date;
    public String subtitle;
    public String web_url;
    public String read;

    public NotificationItem(String id, String title, String subtitle, String date, String web_url,String read) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.web_url = web_url;
        this.read = read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
