package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 10/31/16.
 */
public class SoundPatch {

    @SerializedName("notification_sound")
    public String notificationSound;

    public SoundPatch(String sound) {
        this.notificationSound = sound;
    }
}
