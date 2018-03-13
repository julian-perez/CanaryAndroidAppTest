package is.yranac.canary.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.adapter.VideoSelectorAdapter;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.model.videoexport.VideoExportResponse;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.services.database.VideoExportDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.EntryUtil.DOWNLOADSTATUS;
import static is.yranac.canary.util.EntryUtil.getDownloadProgress;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SHARE_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SHARE_DOWNLOAD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SHARE_VIDEO;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_ENTRY;

/**
 * Created by Schroeder on 1/19/16.
 */
public class ShareUtil extends BaseExportUtil {

    private static final String LOG_TAG = "ShareUtil";

    public static void beforeNeededExportAction(@NonNull final BaseActivity context,
                                                @NonNull final Entry entry) {
        DOWNLOADSTATUS downloadstatus = getDownloadProgress(entry);


        switch (downloadstatus) {
            case NOT_STARTED:
                requestDownloads(context, entry, true);
                GoogleAnalyticsHelper.trackEntry(ACTION_SHARE_VIDEO,
                        PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
                break;
            case IN_PROGRESS:
                AlertDialog alertDialog = AlertUtils.showProcessingVideoAlert(context, true,
                        VideoExportDatabaseService.getVideoExportsByEntry(entry.id));
                context.showDialog(alertDialog);
                break;
            case DOWNLOAD_READY:
                if (Utils.isWifi(context)) {
                    fetchDownloadUrl(context, entry);
                } else {
                    showWifiAlert(context, entry);
                }
                break;

        }
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

    private static boolean pendingCall = false;

    public static void fetchDownloadUrl(@NonNull final BaseActivity context,
                                        @NonNull final Entry entry) {

        if (!context.hasInternetConnection())
            return;

        if (pendingCall)
            return;


        pendingCall = true;

        EntryAPIService.getVideoExports(entry.id, true, new Callback<VideoExportResponse>() {
            @Override
            public void success(VideoExportResponse videoExportResponse, Response response) {
                List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
                selectVideoDownload(context, entry,
                        thumbnails,
                        videoExportResponse.videoExports);
                pendingCall = false;

            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    AlertDialog alertDialog = AlertUtils.showGenericAlert(context, Utils.getErrorMessageFromRetrofit(context, error));
                    context.showDialog(alertDialog);
                } catch (JSONException ignored) {
                }
                pendingCall = false;

            }
        });
    }

    private static void selectVideoDownload(@NonNull final BaseActivity context,
                                            @NonNull final Entry entry,
                                            @NonNull final List<Thumbnail> thumbnails,
                                            @NonNull final List<VideoExport> exports) {
        if (thumbnails.isEmpty() || exports.isEmpty()) {
            return;
        }


        for (VideoExport videoExport : exports) {
            for (Thumbnail thumbnail : thumbnails) {
                if (videoExport.deviceUUID.equalsIgnoreCase(Utils.getStringFromResourceUri(thumbnail.device))) {
                    videoExport.thumbnail = thumbnail.imageUrl();
                    Device device = DeviceDatabaseService.getDeviceFromResourceUri(thumbnail.device);
                    if (device == null)
                        continue;

                    videoExport.name = device.name;
                    videoExport.deviceId = device.id;
                    break;
                }
            }
        }

        Collections.sort(exports, new Comparator<VideoExport>() {
            @Override
            public int compare(VideoExport lhs, VideoExport rhs) {
                if (lhs.deviceId < rhs.deviceId)
                    return -1;
                else if (lhs.deviceId > rhs.deviceId)
                    return 1;
                else
                    return 0;
            }
        });
        if (exports.size() == 1) {
            new DownloadTask(context, exports.get(0), thumbnails.get(0).imageUrl(),
                    entry);
            return;
        }


        LayoutInflater inflater = LayoutInflater.from(context);
        final View alertView = inflater.inflate(R.layout.alert_dialog_select_video,
                (ViewGroup) context.findViewById(android.R.id.content), false);


        ListView listView = (ListView) alertView.findViewById(R.id.selection_list_view);

        VideoSelectorAdapter adapter = new VideoSelectorAdapter(context, exports);
        listView.setAdapter(adapter);


        final AlertDialog alertDialog = buildAlert(context, alertView, false);
        alertDialog.show();

        Button okayButton = (Button) alertView.findViewById(R.id.cancel_btn);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();

                VideoExport videoExport = exports.get(position);
                Thumbnail thumbnail = thumbnails.get(position);
                new DownloadTask(context, videoExport, thumbnail.imageUrl,
                        entry);

            }
        });
    }

    public static AlertDialog buildAlert(Context context, View bodyView, boolean isCancellable) {
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setView(bodyView, 0, 0, 0, 0);
        alert.setCancelable(isCancellable);
        alert.setCanceledOnTouchOutside(isCancellable);
        alert.getWindow().setGravity(Gravity.CENTER);
        int width = (int) context.getResources().getDimension(R.dimen.alert_view_width);
        int height = (int) context.getResources().getDimension(R.dimen.alert_view_height);
        alert.show();
        alert.getWindow().setLayout(width, height);
        return alert;
    }

    private static class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        private BaseActivity context;
        private final VideoExport videoExport;
        private final String image;
        private final Entry entry;
        private PowerManager.WakeLock mWakeLock;
        private AlertDialog mProgressDialog;

        private File video;
        private View progressBar;

        public DownloadTask(@NonNull final BaseActivity context,
                            @NonNull final VideoExport videoExport,
                            @NonNull String image,
                            @NonNull Entry entry) {
            this.context = context;
            this.videoExport = videoExport;
            this.image = image;
            this.entry = entry;
            execute(videoExport.downloadUrl);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater inflater = LayoutInflater.from(context);
            final View alertView = inflater.inflate(R.layout.alert_dialog_download_in_progress,
                    (ViewGroup) context.findViewById(android.R.id.content), false);

            ImageView imageView = (ImageView) alertView.findViewById(R.id.alert_icon);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(image, imageView);

            alertView.findViewById(R.id.check_mark).setAlpha(0.0f);

            int duration = videoExport.duration;
            float size = videoExport.size;

            float sizeMB = (int) (size / Constants.Kibi);

            float durationSec = duration % 60;
            float durationMin = duration / 60;

            DecimalFormat format = new DecimalFormat("##");
            format.setMinimumIntegerDigits(2);

            format.setRoundingMode(RoundingMode.HALF_UP);
            TextView videoLengthTextView = (TextView) alertView.findViewById(R.id.video_length_text_view);
            String time = context.getString(R.string.time_format, format.format(durationMin), format.format(durationSec));
            videoLengthTextView.setText(time);

            TextView videoSizeTextView = (TextView) alertView.findViewById(R.id.video_size_text_view);
            format.setMinimumIntegerDigits(1);

            if (size < Constants.Kibi) {
                videoSizeTextView.setText(context.getString(R.string.kb_format, format.format(size)));
            } else {
                videoSizeTextView.setText(context.getString(R.string.mb_format, format.format(sizeMB)));
            }

            progressBar = alertView.findViewById(R.id.progress_bar);
            mProgressDialog = buildAlert(context, alertView, false);
            context.showDialog(mProgressDialog);

            Button okayButton = (Button) alertView.findViewById(R.id.cancel_btn);
            okayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAnalyticsHelper.trackEntry(ACTION_SHARE_CANCEL, null, null, entry.getLocationId(), entry.id);
                    mProgressDialog.dismiss();
                    cancel(true);
                }
            });
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
        }

        @Override
        protected Boolean doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                String folder = "/files/";
                File direct = new File(context.getFilesDir()
                        + folder);

                if (!direct.exists()) {
                    direct.mkdirs();
                }


                Uri uri = Uri.parse(videoExport.downloadUrl);
                String scheme = uri.getScheme();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-hh-mm", Locale.ENGLISH);
                String deviceName = URLEncoder.encode(videoExport.name.replace(" ", "_"), "UTF-8");
                String title = "canary_" + dateFormat.format(entry.startTime.getTime()) +
                        "_" + deviceName + ".mp4";
                if (scheme == null || (!scheme.equals("http") && !scheme.equals("https")))
                    return false;
                video = new File(direct, title);
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return false;
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(video.getPath());

                int fourKiloBytes = (int) (4 * Constants.Kibi);
                byte data[] = new byte[fourKiloBytes];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return false;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(final Integer... progress) {
            super.onProgressUpdate(progress);

            int width = ((View) progressBar.getParent()).getWidth();
            final int barWidth = (int) (width * (progress[0] / 100.0f));

            RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();

            layout.width = barWidth;
            progressBar.setLayoutParams(layout);
        }

        @Override
        protected void onPostExecute(Boolean completed) {

            if (context.isFinishing())
                return;

            if (!completed || context.isPaused()) {
                mProgressDialog.dismiss();
                return;
            }


            TextView textView = (TextView) mProgressDialog.findViewById(R.id.alert_header);
            if (textView == null)
                return;
            textView.setText(R.string.download_complete);
            View checkMark = mProgressDialog.findViewById(R.id.check_mark);
            checkMark.setAlpha(1.0f);
            checkMark.invalidate();
            mWakeLock.release();
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(1000);
            checkMark.setAnimation(alphaAnimation);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mProgressDialog.dismiss();
                    Uri contentUri = FileProvider.getUriForFile(context, Constants.AUTHORITY_FILES, video);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "#caughtbyCanary");
                    shareIntent.setType("video/mp4");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    context.startActivity(shareIntent);
                    GoogleAnalyticsHelper.trackEntry(ACTION_SHARE_DOWNLOAD,
                            PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alphaAnimation.startNow();

        }
    }
}
