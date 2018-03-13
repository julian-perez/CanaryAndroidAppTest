package is.yranac.canary.util;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.model.videoexport.VideoExportResponse;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.VideoExportDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_EXPORT_DOWNLOAD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_EXPORT_REQUESTED;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_ENTRY;

/**
 * Created by Schroeder on 1/25/16.
 */
public class ExportUtil extends BaseExportUtil {


    public static void beforeNeededExportAction(@NonNull final BaseActivity context,
                                                @NonNull final Entry entry) {

        EntryUtil.DOWNLOADSTATUS downloadstatus = EntryUtil.getDownloadProgress(entry);


        if (downloadstatus == EntryUtil.DOWNLOADSTATUS.DOWNLOAD_READY) {
            if (Utils.isWifi(context)) {
                fetchDownloadUrl(context, entry);
            } else {
                showWifiAlert(context, entry);
            }
        } else if (downloadstatus == EntryUtil.DOWNLOADSTATUS.IN_PROGRESS) {
            context.hideMoreOptionsLayout();
            AlertDialog alertDialog = AlertUtils.showProcessingVideoAlert(context, false,
                    VideoExportDatabaseService.getVideoExportsByEntry(entry.id));
            context.hideMoreOptionsLayout();
            context.showDialog(alertDialog);
        } else {
            GoogleAnalyticsHelper.trackEntry(ACTION_EXPORT_REQUESTED,
                    PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
            requestDownloads(context,
                    entry,
                    false);
        }
    }


    public static void fetchDownloadUrl(@NonNull final BaseActivity context,
                                        @NonNull final Entry entry) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            context.handlePermissionIssue();

            return;
        }


        if (!context.hasInternetConnection())
            return;
        EntryAPIService.getVideoExports(entry.id, false, new Callback<VideoExportResponse>() {
            @Override
            public void success(VideoExportResponse videoExportResponse, Response response) {
                downloadLoadVideos(context,
                        entry,
                        videoExportResponse.videoExports);

                context.hideMoreOptionsLayout();

            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    AlertDialog alertDialog = AlertUtils.showGenericAlert(context, Utils.getErrorMessageFromRetrofit(context, error));
                    context.showDialog(alertDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showWifiAlert(@NonNull final BaseActivity context,
                                     @NonNull final Entry entry) {
        if (!context.hasInternetConnection())
            return;
        AlertDialog alertDialog = AlertUtils.showDwonloadWifiAlert(context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchDownloadUrl(context, entry);

                    }
                }
        );

        context.showDialog(alertDialog);

    }

    private static void downloadLoadVideos(@NonNull final BaseActivity context,
                                           @NonNull final Entry entry,
                                           @NonNull List<VideoExport> videoExports) {

        if (!context.hasInternetConnection())
            return;

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-hh-mm", Locale.ENGLISH);

        String folder = "/canary/exports";
        File direct = new File(Environment.getExternalStorageDirectory()
                + folder);

        if (!direct.exists()) {
            direct.mkdirs();
        }


        for (VideoExport videoExport : videoExports) {
            Log.i("Export", videoExport.downloadUrl);
            Uri uri = Uri.parse(videoExport.downloadUrl);
            String scheme = uri.getScheme();
            String deviceUri = Utils.buildResourceUri(Constants.DEVICE_URI, videoExport.deviceUUID);
            Device device = DeviceDatabaseService.getDeviceFromResourceUri(deviceUri);
            try {
                videoExport.name = URLEncoder.encode(device.name.replace(" ", "_") , "UTF-8");
            } catch (UnsupportedEncodingException ignored) {

            }

            String title = "canary_" + dateFormat.format(entry.startTime.getTime()) + "_"
                    + videoExport.name + ".mp4";
            if (scheme == null || (!scheme.equals("http") && !scheme.equals("https")))
                continue;
            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setTitle(title)
                    .setDestinationInExternalPublicDir(folder, title);
            request.allowScanningByMediaScanner();
            dm.enqueue(request);

        }


        GoogleAnalyticsHelper.trackEntry(ACTION_EXPORT_REQUESTED,
                ACTION_EXPORT_DOWNLOAD, null, entry.getLocationId(), entry.id);
        String goToDownload = context.getString(R.string.go_to_download);
        String okay = context.getString(R.string.okay);
        String downloadStarted = context.getString(R.string.download_started);
        String downloadStartedDsc = context.getString(R.string.download_started_dsc);
        int downloadImage = R.drawable.row_downoload_icon;

        AlertDialog alertDialog = AlertUtils.showGenericAlert(context, downloadStarted, downloadStartedDsc, downloadImage, okay, goToDownload, 0, 0,
                null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    }
                });
        context.hideMoreOptionsLayout();
        context.showDialog(alertDialog);
    }
}
