package is.yranac.canary.util;

import java.util.List;

import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.services.database.VideoExportDatabaseService;

/**
 * Created by Schroeder on 6/18/15.
 */
public class EntryUtil {


    public enum DOWNLOADSTATUS {
        NOT_STARTED,
        IN_PROGRESS,
        DOWNLOAD_READY;

    }

    public static DOWNLOADSTATUS getDownloadProgress(Entry entry) {

        boolean requested = false;
        boolean finished = true;

        if (!entry.exported) {
            finished = false;
            requested = didRequest(entry);

        }

        if (finished) {
            return DOWNLOADSTATUS.DOWNLOAD_READY;
        } else if (requested) {
            return DOWNLOADSTATUS.IN_PROGRESS;
        } else {
            return DOWNLOADSTATUS.NOT_STARTED;
        }

    }

    public static boolean didRequest(Entry entry) {
        List<VideoExport> videoExports = VideoExportDatabaseService.getVideoExportsByEntry(entry.id);
        for (VideoExport videoExport : videoExports) {
            long requestTimeElapse = DateUtil.getCurrentTime().getTime() - videoExport.requestedAt.getTime();
            if (videoExport.processing && requestTimeElapse < 60 * 60 * 6 * 1000)
                return true;
        }
        return false;
    }
}
