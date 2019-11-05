package org.app.mbooster.model_folder;

/**
 * Created by Mobkini on 8/2/2018.
 */

public class ButtonCountdown {
    public String id;
    public long coundowntime;

    public ButtonCountdown(String id, long coundowntime) {
        this.id = id;
        this.coundowntime = coundowntime;
    }

    public void update_countdowntime(long coundowntime){
        this.coundowntime = coundowntime;
    }
}
