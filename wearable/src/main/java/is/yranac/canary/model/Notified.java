package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 10/6/15.
 */
public class Notified {

    @SerializedName("detection_score")
    public String detectionScore;

    @SerializedName("event_id")
    public long eventId;

    @SerializedName("person_score")
    public String personScore;
}
