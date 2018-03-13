package is.yranac.canary.model.reading;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

public class ReadingResponse {
    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Reading> readings;
}
