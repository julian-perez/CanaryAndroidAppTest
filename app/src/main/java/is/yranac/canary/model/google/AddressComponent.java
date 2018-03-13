package is.yranac.canary.model.google;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 9/17/14.
 */
public class AddressComponent {

    @SerializedName("long_name")
    public String longName;

    @SerializedName("short_name")
    public String shortName;

    @SerializedName("types")
    private List<String> types;
}
