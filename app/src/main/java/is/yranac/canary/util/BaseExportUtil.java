package is.yranac.canary.util;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import org.json.JSONException;

import is.yranac.canary.messages.InsertVideos;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.videoexport.VideoExportResponse;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.ui.BaseActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Schroeder on 3/21/16.
 */
public class BaseExportUtil {
    public interface ExportInterface {
        void showDialog(AlertDialog dialog);

        void hideMoreOptionsLayout();

        void handlePermissionIssue();
    }

    private static boolean pendingCall = false;
    protected static void requestDownloads(@NonNull final BaseActivity context,
                                           @NonNull final Entry entry,
                                           final boolean share) {

        if (!context.hasInternetConnection())
            return;

        if (pendingCall)
            return;

        pendingCall = true;

        EntryAPIService.getVideoExports(entry.id, share, new Callback<VideoExportResponse>() {
            @Override
            public void success(VideoExportResponse videoExportResponse, Response response) {

                if (context.isFinishing())
                    return;

                TinyMessageBus.post(new InsertVideos(videoExportResponse.videoExports, entry.id));
                AlertDialog openAlert = AlertUtils.showProcessingVideoAlert(context, share,
                        videoExportResponse.videoExports);

                if (!share)
                    context.hideMoreOptionsLayout();

                context.showDialog(openAlert);
                pendingCall = false;
            }

            @Override
            public void failure(RetrofitError error) {
                if (context.isFinishing())
                    return;
                try {
                    AlertDialog alertDialog = AlertUtils.showGenericAlert(context, Utils.getErrorMessageFromRetrofit(context, error));

                    context.showDialog(alertDialog);

                } catch (JSONException ignored) {
                }
                pendingCall = false;

            }
        });
    }

}
