package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 3/29/16.
 */
public class DeviceType {

    public static final int CANARY = 1;
    public static final int CANARY_PLUS = 2;
    public static final int FLEX = 3;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    public DeviceType() {
    }

    public DeviceType(int typeId) {
        this.id = typeId;

        switch (id)
        {
            case CANARY:
                name = "Canary";
                break;
            case CANARY_PLUS:
                name = "Canary Plus";
                break;
            case FLEX:
                name = "Canary Flex";
                break;
        }
    }

}
