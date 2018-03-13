package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Entry {

    public static final int SHOWING_ALL = 1;
    public static final int SHOWING_AWAY_MODE = 2;
    public static final int SHOWING_FLAGGED = 3;
    public static final int DAILY_TIMELINE = 4;


    public final static String ENTRY_TYPE_MOTION = "motion";
    public final static String ENTRY_TYPE_LIVE = "live";

    public final static String ENTRY_TYPE_CONNECT = "connect";
    public final static String ENTRY_TYPE_DISCONNECT = "disconnect";
    public final static String ENTRY_TYPE_HUMIDITY = "humidity";
    public final static String ENTRY_TYPE_AIR_QUALITY = "air";
    public final static String ENTRY_TYPE_TEMPERATURE = "temperature";
    public final static String ENTRY_TYPE_SIREN = "siren";
    public final static String ENTRY_TYPE_OTA = "ota";
    public final static String ENTRY_TYPE_LOCATION = "location";
    public static final String ENTRY_TYPE_MODE = "mode";


    @SerializedName("id")
    public long id;

    @SerializedName("comments")
    public List<Comment> comments = new ArrayList<>();

    @SerializedName("description")
    public String description;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("thumbnails")
    public List<Thumbnail> thumbnails;

    @SerializedName("starred")
    public boolean starred;

    @SerializedName("deleted")
    public boolean deleted;

    @SerializedName("last_modified")
    public Date lastModified;

    @SerializedName("start_time")
    public Date startTime;

    @SerializedName("location")
    public String locationUri;

    @SerializedName("end_time")
    public Date endTime;

    @SerializedName("exported")
    public boolean exported;

    @SerializedName("duration")
    public int duration;

    @SerializedName("entry_type")
    public String entryType;

    @SerializedName("labels")
    public List<Label> labels;

    @SerializedName("device_mode")
    public Mode deviceMode;

    @SerializedName("customers")
    public List<String> customers;

    @SerializedName("video_exports")
    public List<VideoExport> videoExports;

    @SerializedName("display_meta")
    public DisplayMeta displayMeta;

    @SerializedName("device_uuids")
    public List<String> deviceUuids;

    public int thumbnailCount;

    public boolean hasDetailView() {
        return entryType != null && (entryType.equalsIgnoreCase(ENTRY_TYPE_MOTION) || entryType.equalsIgnoreCase(ENTRY_TYPE_LIVE));
    }

    public boolean isHomeHealthNotificationEntry() {
        return entryType != null && (
                entryType.equals(ENTRY_TYPE_HUMIDITY)
                        || entryType.equals(ENTRY_TYPE_TEMPERATURE)
                        || entryType.equals(ENTRY_TYPE_AIR_QUALITY)
        );
    }

    public boolean isOfflineEntry() {
        return entryType != null && (
                entryType.equals(ENTRY_TYPE_CONNECT)
                        || entryType.equals(ENTRY_TYPE_DISCONNECT)
        );
    }

}
