package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Schroeder on 4/29/16.
 */
public class LocationNightModeSchedule {

    @SerializedName("location")
    public String locationUri;

    @SerializedName("start_time")
    public String startTime;

    @SerializedName("end_time")
    public String endTime;

    public LocationNightModeSchedule(String locationUri, String startTime, String endTime) {
        this.locationUri = locationUri;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
