package is.yranac.canary.model.entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 4/17/15.
 */
public class EntryDeletePatch {

    @SerializedName("deleted")
    public final boolean deleted;

    public EntryDeletePatch() {
       this.deleted =true;
   }
}
