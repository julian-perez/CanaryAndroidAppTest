package is.yranac.canary.model.subscription;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Schroeder on 5/5/15.
 */
public class ServiceProfile {


    @SerializedName("csatOverride")
    public boolean csatOverride;

    @SerializedName("timelineLength")
    public int timeLineLength;

    @SerializedName("expiresOn")
    public Date expiresOn;


}
