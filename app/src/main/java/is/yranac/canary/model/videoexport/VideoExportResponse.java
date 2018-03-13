package is.yranac.canary.model.videoexport;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 5/5/15.
 */
public class VideoExportResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<VideoExport> videoExports;
}
