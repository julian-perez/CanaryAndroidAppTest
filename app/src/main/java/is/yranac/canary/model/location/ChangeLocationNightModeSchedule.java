package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Schroeder on 5/2/16.
 */
public class ChangeLocationNightModeSchedule {

    @SerializedName("start_time")
    public String startTime;

    @SerializedName("end_time")
    public String endTime;

    public ChangeLocationNightModeSchedule(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
