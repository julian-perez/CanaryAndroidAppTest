package is.yranac.canary.model.clip;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 8/11/14.
 */
public class ClipsResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Clip> clips;
}
