package is.yranac.canary.model.emergencycontacts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 10/4/15.
 */
public class EmergencyContactsResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<EmergencyContact> emergencyContactList;

}
