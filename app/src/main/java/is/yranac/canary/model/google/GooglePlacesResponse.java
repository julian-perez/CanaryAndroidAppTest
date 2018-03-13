package is.yranac.canary.model.google;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 9/17/14.
 */
public class GooglePlacesResponse {

    @SerializedName("html_attributions")
    public List<String> htmlAttributions;

    @SerializedName("next_page_token")
    public String nextPageToken;

    @SerializedName("results")
    public List<GooglePlace> results;

    @SerializedName("status")
    public String status;
}
