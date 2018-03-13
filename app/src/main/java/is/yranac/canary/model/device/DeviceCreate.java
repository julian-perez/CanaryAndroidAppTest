package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

public class DeviceCreate {

    @SerializedName("serial_number")
    String serial;

    @SerializedName("location")
    String location;

    @SerializedName("name")
    String name;



    public DeviceCreate (String serial, String locationUri, String name) {
        this.serial = serial;
        this.location = locationUri;
        this.name = name;
    }
}
