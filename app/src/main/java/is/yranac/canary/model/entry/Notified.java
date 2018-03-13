package is.yranac.canary.model.entry;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Schroeder on 10/6/15.
 */
public class Notified {

    @SerializedName("detection_threshold")
    public double detectionThreshold;

    @SerializedName("device_uuid")
    public String deviceUUID;

    @SerializedName("event_created")
    public Date eventCreated;

    @SerializedName("event_id")
    public long eventId;

    @SerializedName("non_background_score")
    public double nonBackgroundScore;

}
