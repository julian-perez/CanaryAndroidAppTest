package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Schroeder on 4/29/15.
 */
public class VideoExport {


    @SerializedName("device_uuid")
    public String deviceUUID;

    @SerializedName("download_url")
    public String downloadUrl;

    @SerializedName("entry")
    public String entry;

    @SerializedName("processing")
    public boolean processing;

    @SerializedName("duration")
    public int duration;

    @SerializedName("size")
    public int size;

    @SerializedName("requested_at")
    public Date requestedAt;

    public String thumbnail;

    public String name;

    public int deviceId;
}
