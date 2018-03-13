package is.yranac.canary.model.featureflag;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 3/1/16.
 */
public class FeatureFlagResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<FeatureFlag> objects;
}
