package is.yranac.canary.model.label;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 9/22/14.
 */
public class LabelsPatch {

    @SerializedName("labels")
    public String labels;

    public LabelsPatch(String labels) {
        this.labels = labels;
    }
}
