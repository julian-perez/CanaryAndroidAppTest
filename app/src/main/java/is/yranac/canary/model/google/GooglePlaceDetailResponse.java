package is.yranac.canary.model.google;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 9/17/14.
 */
public class GooglePlaceDetailResponse {

    @SerializedName("html_attributions")
    public List<String> htmlAttributions;

    @SerializedName("result")
    public GooglePlaceDetails result;

    @SerializedName("status")
    public String status;
}
