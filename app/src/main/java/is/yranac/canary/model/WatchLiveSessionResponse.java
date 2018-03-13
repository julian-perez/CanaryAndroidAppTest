package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michaelschroeder on 5/3/17.
 */

public class WatchLiveSessionResponse {


    @SerializedName("objects")
    public List<WatchLiveSession> sessions;
}
