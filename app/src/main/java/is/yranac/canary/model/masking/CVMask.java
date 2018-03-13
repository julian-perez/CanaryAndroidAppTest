package is.yranac.canary.model.masking;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.util.StringUtils;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class CVMask {

    @SerializedName("device")
    public String deviceURL;

    @SerializedName("name")
    public String name;

    @SerializedName("vertices")
    public List<CVVertex> vertices;

    @SerializedName("mask_in")
    public boolean maskIn;

    public CVMask() {
        this.vertices = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CVMask) || StringUtils.isNullOrEmpty(this.name))
            return false;

        CVMask compareTo = (CVMask) obj;
        if (StringUtils.isNullOrEmpty(compareTo.name))
            return false;

        return this.name.equals(compareTo.name);
    }

    public CVMask clone() {
        CVMask mask = new CVMask();

        mask.deviceURL = this.deviceURL;
        mask.maskIn = this.maskIn;
        mask.name = this.name;
        mask.vertices = new ArrayList<>();

        for (CVVertex v : this.vertices) {
            mask.vertices.add(v.clone());
        }

        return mask;
    }
}
