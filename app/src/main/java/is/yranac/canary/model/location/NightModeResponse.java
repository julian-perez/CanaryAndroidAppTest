package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 4/29/16.
 */
public class NightModeResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<NightModeSchedule> nightmodes;
}
