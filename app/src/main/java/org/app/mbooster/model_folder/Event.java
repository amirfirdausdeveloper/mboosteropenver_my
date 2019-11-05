package org.app.mbooster.model_folder;

/**
 * Created by royfei on 07/11/2017.
 */

public class Event {

    public String event_id,
            event_name,
            event_start_datetime,
            event_end_datetime,
            event_desc,
            event_img,
            event_start_button;

    public Event(String event_id, String event_name, String event_start_datetime,String event_end_datetime, String event_desc, String event_img ,String event_start_button) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_start_datetime = event_start_datetime;
        this.event_end_datetime = event_end_datetime;
        this.event_desc = event_desc;
        this.event_img = event_img;
        this.event_start_button = event_start_button;
    }
}
