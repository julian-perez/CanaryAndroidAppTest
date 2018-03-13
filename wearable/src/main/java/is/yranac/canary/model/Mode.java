package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.utils.Constants;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Mode {

    public static final String ERROR = Constants.MODES_URI + "0/";

    public static final String MODE_PRIVACY = "Privacy";
    public static final String MODE_HOME=  "Home";
    public static final String MODE_AWAY  = "Away";
    public static final String MODE_NIGHT = "Night";

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("resource_uri")
    public String resourceUri;

    public Mode(int id) {
        this.id = id;
    }

    public Mode(){

    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Mode))
            return false;

        Mode mode = (Mode) obj;


        return mode.id == this.id;


    }
}
