package is.yranac.canary.model.clip;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Clip {

    @SerializedName("device")
	public String device;

    @SerializedName("duration")
	public int duration;

    @SerializedName("end")
	public Date end;

    @SerializedName("start")
	public Date start;

}
