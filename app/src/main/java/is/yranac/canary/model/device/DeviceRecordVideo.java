package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

public class DeviceRecordVideo {

    @SerializedName("device")
    public String device;

    @SerializedName("start")
    public String start;

    @SerializedName("end")
    public String end;

    public DeviceRecordVideo(String device, String start, String end) {
        this.device = device;
        this.start = start;
        this.end = end;
    }
}
