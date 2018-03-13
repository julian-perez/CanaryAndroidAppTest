package is.yranac.canary.model.locationchange;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 8/3/15.
 */
public class ClientLocationList {

    @SerializedName("objects")
    public List<ClientLocation> objects;

    public ClientLocationList(List<ClientLocation> objects){
        this.objects = objects;
    }
}
