package is.yranac.canary.util;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.messages.ShareCompletedProcessing;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.EntryDetailActivity;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Schroeder on 4/8/15.
 */

public class PushUtils {

    private static final String LOG_TAG = "PushUtils";

    public static void showPush(final BaseActivity activity, final PushReceived pushReceived) {
        if (!activity.isPushEnabled())
            return;

        final String title = pushReceived.title;
        final String entryIdAsString = pushReceived.entryId;

        if (entryIdAsString != null) {
            long entryId = entryIdAsString.equals("") ? 0 : Long.parseLong(entryIdAsString);
            EntryAPIService.getEntryById(
                    entryId, new Callback<Entry>() {
                        @Override
                        public void success(Entry entry, Response response) {
                            entry.lastModified = new Date(0);
                            EntryDatabaseService.insertEntryAndLinkedObjects(entry, Entry.SHOWING_ALL);
                            if (!activity.isFinishing()) {

                                if (pushReceived.entryType.equals("entry_exported") ||
                                        pushReceived.entryType.equals("entry_exported_share")) {
                                    TinyMessageBus.post(new ShareCompletedProcessing(entry.id));
                                    showDownloadVideoAlert(activity, entry, pushReceived);
                                } else if (entry.isOfflineEntry()) {
                                    showOnOffAlert(activity, entry);
                                } else {
                                    showPushAlertDialog(activity, title, entry);
                                }
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
        } else {
            AlertUtils.showGenericAlert(activity, pushReceived.title);
        }

    }

    private static void showOnOffAlert(final BaseActivity activity, Entry entry) {
        if (!entry.isOfflineEntry())
            return;

        boolean isOnLine = entry.entryType.equalsIgnoreCase(Entry.ENTRY_TYPE_CONNECT);

        String leftBtnText;
        String rightBtnText;
        View.OnClickListener rightOnClickLister;
        if (!isOnLine) {
            leftBtnText = activity.getString(R.string.okay);
            rightBtnText = activity.getString(R.string.view_tips);
            rightOnClickLister = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = activity.getString(R.string.connectivity_url);
                    ZendeskUtil.loadHelpCenter(activity, url);
                }
            };
        } else {
            leftBtnText = activity.getString(R.string.okay);
            rightBtnText = null;
            rightOnClickLister = null;
        }
        AlertUtils.showGenericAlert(activity, entry.description, null, 0, leftBtnText,
                rightBtnText, 0, 0, null, rightOnClickLister);
    }


    private static void showDownloadVideoAlert(final BaseActivity activity, final Entry entry, final PushReceived pushReceived) {

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return;

        LayoutInflater inflater = LayoutInflater.from(activity);
        View alertView = inflater.inflate(R.layout.alert_dialog_download_ready, null);

        ImageView imageView = (ImageView) alertView.findViewById(R.id.alert_icon);
        List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);

        if (thumbnails.size() > 0) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            Thumbnail thumbnail = thumbnails.get(0);
            imageLoader.displayImage(
                    thumbnail.imageUrl(), imageView);

        } else {
            imageView.setVisibility(View.GONE);
        }

        if (pushReceived.entryType.equalsIgnoreCase("entry_exported_share")) {
            TextView alertHeader =
                    (TextView) alertView.findViewById(R.id.alert_header);
            alertHeader.setText(R.string.share_is_ready);
        }


        final AlertDialog alert = AlertUtils.buildAlert(activity, alertView, true);
        alert.show();

        float sizeMB = pushReceived.videoExportSize / Constants.Kibi;

        float durationSec = pushReceived.videoExportDuration % 60;
        float durationMin = pushReceived.videoExportDuration / 60;


        DecimalFormat format = new DecimalFormat("##");
        format.setMinimumIntegerDigits(2);

        format.setRoundingMode(RoundingMode.HALF_UP);

        TextView videoLengthTextView = (TextView) alertView.findViewById(R.id.video_length_text_view);
        videoLengthTextView.setText(format.format(durationMin) + ":" + format.format(durationSec));

        TextView videoSizeTextView = (TextView) alertView.findViewById(R.id.video_size_text_view);
        format.setMinimumIntegerDigits(1);

        if (pushReceived.videoExportSize < Constants.Kibi) {
            videoSizeTextView.setText(activity.getString(R.string.kb_format, format.format(pushReceived.videoExportSize)));
        } else {
            videoSizeTextView.setText(activity.getString(R.string.mb_format, format.format(sizeMB)));
        }
        final int oldOrientation = activity.getRequestedOrientation();
        Button retryBtn = (Button) alert.findViewById(R.id.alert_button_left);
        retryBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.setRequestedOrientation(oldOrientation);
                        alert.dismiss();
                    }
                });


        Button getHelpBtn = (Button) alert.findViewById(R.id.alert_button_right);
        if (pushReceived.entryType.equalsIgnoreCase("entry_exported_share")) {
            getHelpBtn.setText(R.string.share);
        }
        getHelpBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        activity.setRequestedOrientation(oldOrientation);
                        alert.dismiss();
                        if (pushReceived.entryType.equalsIgnoreCase("entry_exported_share")) {
                            ShareUtil.beforeNeededExportAction(activity, entry);
                            return;
                        }
                        if (activity instanceof EntryDetailActivity) {
                            EntryDetailActivity entryDetailActivity = (EntryDetailActivity) activity;

                            Entry mEntry = entryDetailActivity.getEntry();
                            if (mEntry != null && mEntry.id == entry.id) {
                                entryDetailActivity.openMenu();
                            }
                            return;
                        }
                        Intent intent = new Intent(activity, EntryDetailActivity.class);
                        intent.putExtra("entryId", entry.id);
                        intent.putExtra("open_menu", true);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                });
        activity.setAlert(alert);

    }

    private static void showPushAlertDialog(final BaseActivity activity, String title, final Entry entry) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View alertView = inflater.inflate(R.layout.alert_dialog_entry_notification, null);
        TextView titleTextView = (TextView) alertView.findViewById(R.id.alert_notification_title_text_view);
        titleTextView.setText(title);

        TextView timestampTextView = (TextView) alertView.findViewById(R.id.alert_notification_date_text_view);
        if (entry != null && entry.startTime != null) {
            Date entryDate = entry.startTime;
            timestampTextView.setText(DateUtil.utcDateToDisplayString(entryDate));
        }

        final AlertDialog alert = AlertUtils.buildAlert(activity, alertView, true);

        alert.show();
        Button gotoEvent = (Button) alertView.findViewById(R.id.alert_button);
        final int oldOrientation = activity.getRequestedOrientation();

        if (entry != null && entry.hasDetailView()) {
            gotoEvent.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trackEvent(entry);
                            activity.setRequestedOrientation(oldOrientation);
                            alert.dismiss();
                            Intent intent = new Intent(activity, EntryDetailActivity.class);
                            intent.putExtra("entryId", entry.id);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
        } else if (entry != null && entry.isHomeHealthNotificationEntry()) {
            gotoEvent.setText(R.string.view_activity);
            gotoEvent.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            trackEvent(entry);
                            if (entry.deviceUuids == null || entry.deviceUuids.size() == 0)
                                return;

                            String deviceUuid = entry.deviceUuids.get(0);
                            activity.showHomehealthFragment(deviceUuid);
                        }
                    });
        } else {
            if (entry != null && entry.isOfflineEntry())
                gotoEvent.setText(R.string.okay);
            else
                gotoEvent.setText(R.string.unsave);
            gotoEvent.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.setRequestedOrientation(oldOrientation);
                            alert.dismiss();
                        }
                    });
        }
        activity.setAlert(alert);
    }

    private static void trackEvent(Entry entry) {
        if (entry == null)
            return;


        GoogleAnalyticsHelper.trackEntry("push_open", "in-app", null, entry.getLocationId(), entry.id);
    }
}
