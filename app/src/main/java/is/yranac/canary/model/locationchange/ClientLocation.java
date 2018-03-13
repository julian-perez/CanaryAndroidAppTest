package is.yranac.canary.model.locationchange;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.MathUtil;

/**
 * Created by Schroeder on 7/31/15.
 */
public class ClientLocation {


    public transient int id;

    @SerializedName("event_type")
    public String type;

    @SerializedName("location_accuracy")
    public float accuracy;

    @SerializedName("client_battery_level")
    public float batteryPct;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;

    @SerializedName("client_uuid")
    public String uuid;

    @SerializedName("time_received_app")
    public String locationDate;

    @SerializedName("time_received_client")
    public String timeReceivedClient;

    @SerializedName("client_wifi_on")
    public boolean wifiOn;

    @SerializedName("client_gps_on")
    public boolean gpsOn;


    public ClientLocation(double lat, double lng, String type, float accuracy, float batteryPct,
                          String uuid, Date date, Date locationDate, boolean wifiOn, boolean gpsOn) {

        this.lat = MathUtil.round(lat,6);
        this.lng =  MathUtil.round(lng,6);
        this.type = type;
        this.accuracy = accuracy;
        this.batteryPct = batteryPct;
        this.uuid = uuid;
        this.gpsOn = gpsOn;
        this.wifiOn = wifiOn;
        this.locationDate =  DateUtil.convertDateToApiString(locationDate);
        this.timeReceivedClient = DateUtil.convertDateToApiString(date);
    }

    public ClientLocation() {

    }
}
