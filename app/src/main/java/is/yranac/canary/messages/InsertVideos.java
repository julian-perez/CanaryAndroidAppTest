package is.yranac.canary.messages;

import java.util.List;

import is.yranac.canary.model.videoexport.VideoExport;

/**
 * Created by Schroeder on 3/28/16.
 */
public class InsertVideos {
    public final List<VideoExport> videoExports;
    public final long entryId;

    public InsertVideos(List<VideoExport> videoExports, long entryId) {

        this.videoExports = videoExports;
        this.entryId = entryId;
    }
}
