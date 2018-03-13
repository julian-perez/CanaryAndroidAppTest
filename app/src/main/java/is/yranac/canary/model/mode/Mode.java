package is.yranac.canary.model.mode;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.Constants;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Mode {

    public static final String ERROR_URI = Constants.MODES_URI + "0/";
    public static final String ERROR = "error";

    public static final int TOTAL_MODES = 7;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("resource_uri")
    public String resourceUri;

    public Mode(int id) {
        this.id = id;
    }

    public Mode() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Mode))
            return false;

        Mode mode = (Mode) obj;


        return mode.id == this.id;


    }

    public static Mode Error() {
        Mode mode = new Mode(0);
        mode.resourceUri = ERROR_URI;
        mode.name = ERROR;
        return mode;
    }
}
