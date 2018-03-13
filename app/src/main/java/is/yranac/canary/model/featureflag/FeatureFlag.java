package is.yranac.canary.model.featureflag;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Schroeder on 3/1/16.
 */
public class FeatureFlag {

    @SerializedName("enabled")
    public boolean enabled;

    @SerializedName("key")
    public String key;

    @SerializedName("metadata")
    public Map<String, String> meta;

    @Override
    public boolean equals(Object o) {
        if (o instanceof FeatureFlag) {
            FeatureFlag label = (FeatureFlag) o;
            return key.equalsIgnoreCase(label.key);
        }
        return false;
    }
}
