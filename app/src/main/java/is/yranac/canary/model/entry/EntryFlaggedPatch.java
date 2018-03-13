package is.yranac.canary.model.entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cumisnic on 8/4/14.
 */
public class EntryFlaggedPatch {

    @SerializedName("starred")
    boolean starred;

    public EntryFlaggedPatch(boolean isStarred) {
        starred = isStarred;
    }
}