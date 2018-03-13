package is.yranac.canary.model.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

public class EntryResponse {
    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Entry> entries;
}
