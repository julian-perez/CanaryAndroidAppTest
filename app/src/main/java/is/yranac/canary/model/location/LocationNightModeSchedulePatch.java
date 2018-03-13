package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Schroeder on 4/29/16.
 */
public class LocationNightModeSchedulePatch {

    @SerializedName("start_time")
    public Date startTime;

    @SerializedName("end_time")
    public Date endTime;

    public LocationNightModeSchedulePatch(Date startTime, Date endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
