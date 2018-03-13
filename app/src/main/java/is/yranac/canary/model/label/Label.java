package is.yranac.canary.model.label;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 9/22/14.
 */
public class Label {

    @SerializedName("name")
    public String name;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("id")
    public int id;

    public long entryId;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Label){
            Label label = (Label) o;
            return name.equalsIgnoreCase(label.name);
        }
        return false;
    }
}
