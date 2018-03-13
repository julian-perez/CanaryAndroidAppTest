package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 4/18/16.
 */
public class LocationDataOptin {

    @SerializedName("is_sharing_data")
    public boolean isSharingData;


    public LocationDataOptin(boolean isSharingData) {
        this.isSharingData = isSharingData;
    }
}
