package is.yranac.canary.adapter.holder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.EntryThumbnailAdapter;
import is.yranac.canary.contentproviders.CanaryCommentContentProvider;
import is.yranac.canary.contentproviders.CanaryLabelContentProvider;
import is.yranac.canary.contentproviders.CanaryNotifiedContentProvider;
import is.yranac.canary.contentproviders.CanaryVideoExportContentProvider;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.Notified;
import is.yranac.canary.model.label.Label;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LabelDatabaseService;
import is.yranac.canary.services.database.NotifiedDatabaseService;
import is.yranac.canary.services.database.VideoExportDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.EntryDetailActivity;
import is.yranac.canary.ui.views.PagerContainer;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.CustomAsyncHandler;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.*;

/**
 * Created by Schroeder on 6/5/15.
 */
public class EntryViewHolderMotion extends EntryViewHolder {
    public final LinearLayout normalEntryLayout;
    public final TextView entryTitleTextView;
    public final TextView dateTextView;
    public final ImageView flaggedButton;
    public final RelativeLayout thumbnailContainer;
    public final ImageView thumbnailView;
    public final ViewPager thumbnailViewPager;
    public final PagerContainer pagerContainer;
    public final View headerLayout;
    public final View mainLayout;
    public final TextView headerTextView;
    public final ImageView exportVideoIcon;
    public final TextView commentNumberTextView;
    public final View commentContainer;
    public final View notifiedIcon;
    public final ImageView commentBubble;
    public final TextView entryLabelTextView;
    public final View tagContainer;

    public EntryViewHolderMotion(BaseActivity activity, View view, ViewGroup viewGroup) {
        super(view, activity);

        this.mainLayout = view.findViewById(R.id.main_layout);
        this.headerLayout = view.findViewById(R.id.header_layout);
        this.headerTextView = (TextView) view.findViewById(R.id.header_text_view);

        this.normalEntryLayout = (LinearLayout) view.findViewById(R.id.normal_entry_layout);

        this.entryTitleTextView = (TextView) view.findViewById(R.id.entry_summary);
        this.dateTextView = (TextView) view.findViewById(R.id.date_text_view);

        this.flaggedButton = (ImageView) view.findViewById(R.id.entry_flag);

        this.thumbnailContainer = (RelativeLayout) view.findViewById(R.id.thumbnail_container);
        this.thumbnailView = (ImageView) view.findViewById(R.id.thumbnail_image_view);

        this.thumbnailViewPager = (ViewPager) view.findViewById(R.id.entry_thumbnail_pager);
        EntryThumbnailAdapter adapter = new EntryThumbnailAdapter(activity);
        this.thumbnailViewPager.setAdapter(adapter);

        this.pagerContainer = (PagerContainer) view.findViewById(R.id.pager_container);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.pagerContainer.getLayoutParams();
        layoutParams.height = (int) (((float) viewGroup.getWidth()) * 9.0f / 16.0f);
        this.pagerContainer.setLayoutParams(layoutParams);

        this.thumbnailViewPager.setOffscreenPageLimit(adapter.getCount());
        this.thumbnailViewPager.setPageMargin(DensityUtil.dip2px(activity, 12f));
        this.thumbnailViewPager.setClipChildren(false);

        this.exportVideoIcon = (ImageView) view.findViewById(R.id.processing_image_view);

        this.commentBubble = (ImageView) view.findViewById(R.id.comment_bubble);
        this.commentNumberTextView = (TextView) view.findViewById(R.id.comment_number_text_view);
        this.commentContainer = view.findViewById(R.id.comment_container);
        this.notifiedIcon = view.findViewById(R.id.notified_icon);
        this.tagContainer = view.findViewById(R.id.tag_container);

        this.entryLabelTextView = (TextView) view.findViewById(R.id.entry_labels);
    }

    private CustomAsyncHandler customAsyncHandler;


    private final int LABEL_QUERY = 234;
    private final int CUSTOMER_QUERY = 124;
    private final int DOWNLOAD_STATUS_QUERY = 2734;
    private final int NOTIFIED_QUERY = 2785;

    private CustomAsyncHandler.AsyncQueryListener asyncQueryListener = new CustomAsyncHandler.AsyncQueryListener() {
        @Override
        public void onQueryComplete(int token, Object cookie, Cursor cursor) {
            switch (token) {
                case LABEL_QUERY:
                    setEntryTitle(cursor);
                    break;
                case CUSTOMER_QUERY:
                    setCommentIconVisible(cursor);
                    break;
                case DOWNLOAD_STATUS_QUERY:
                    setDownloadStatus(cursor);
                    break;
                case NOTIFIED_QUERY:
                    setNotifiedIcon(cursor);
                    break;
            }
            cursor.close();

        }
    };

    private void setNotifiedIcon(Cursor cursor) {
        Notified notified = null;
        if (cursor.moveToFirst()) {
            notified = NotifiedDatabaseService.notifiedFromCursor(cursor);
        }
        if (notified != null) {
            notifiedIcon.setVisibility(View.VISIBLE);
            commentContainer.setVisibility(View.VISIBLE);
        } else {
            notifiedIcon.setVisibility(View.GONE);
        }
    }


    private void setEntryTitle(Cursor cursor) {
        List<Label> labels = new ArrayList<>();
        while (cursor.moveToNext()) {
            labels.add(LabelDatabaseService.getLabelFromCursor(cursor));
        }

        String labelString = LabelDatabaseService.createLabelString(labels);

        if (labels.size() > 0) {
            entryLabelTextView.setText(labelString);
            tagContainer.setVisibility(View.VISIBLE);
        } else {
            tagContainer.setVisibility(View.GONE);
        }
    }

    private void setCommentIconVisible(Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0) {
            commentNumberTextView.setText(String.valueOf(cursor.getCount()));
            commentBubble.setImageResource(R.drawable.timeline_icn_commented);
            commentNumberTextView.setTextColor(context.getResources().getColor(R.color.darker_gray));
        } else {
            commentNumberTextView.setText("0");
            commentBubble.setImageResource(R.drawable.timeline_icn_comment);
            commentNumberTextView.setTextColor(context.getResources().getColor(R.color.gray));
        }

    }

    private void setDownloadStatus(Cursor cursor) {
        List<VideoExport> videoExports = new ArrayList<>();
        while (cursor.moveToNext()) {
            videoExports.add(VideoExportDatabaseService.getVideoExportFromCursor(cursor));
        }

        boolean requested = false;
        boolean finished = true;

        for (VideoExport videoExport : videoExports) {
            long requestTimeElapse = new Date().getTime() - videoExport.requestedAt.getTime();
            if (videoExport.processing && requestTimeElapse < 60 * 60 * 6 * 1000)
                requested = true;

            if (videoExport.downloadUrl == null || videoExport.downloadUrl.length() == 0)
                finished = false;

        }


        if (finished && !requested) {
            exportVideoIcon.setVisibility(View.VISIBLE);
            exportVideoIcon.setImageResource(R.drawable.timeline_icn_download);
        } else if (requested) {
            exportVideoIcon.setVisibility(View.VISIBLE);
            exportVideoIcon.setImageResource(R.drawable.timeline_icn_process);
        } else {
            exportVideoIcon.setVisibility(View.GONE);
        }

    }

    public void setUpEntryDescription(Entry entry) {

        if (customAsyncHandler != null) {
            customAsyncHandler.cancelOperation(LABEL_QUERY);
        } else {
            customAsyncHandler = new CustomAsyncHandler(context.getContentResolver(), asyncQueryListener);
        }

        String where = CanaryLabelContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = new String[]{String.valueOf(entry.id)};


        customAsyncHandler.startQuery(LABEL_QUERY, null, CanaryLabelContentProvider.CONTENT_URI,
                null, where, whereArgs, null);

    }

    public void setCommentIconVisibility(Entry entry) {

        if (customAsyncHandler != null) {
            customAsyncHandler.cancelOperation(CUSTOMER_QUERY);
        } else {
            customAsyncHandler = new CustomAsyncHandler(context.getContentResolver(), asyncQueryListener);
        }

        String where = CanaryCommentContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = new String[]{String.valueOf(entry.id)};
        customAsyncHandler.startQuery(CUSTOMER_QUERY, null, CanaryCommentContentProvider.CONTENT_URI,
                null, where, whereArgs, null);
    }

    public void setDownloadIconVisibility(Entry entry) {

        if (customAsyncHandler != null) {
            customAsyncHandler.cancelOperation(DOWNLOAD_STATUS_QUERY);
        } else {
            customAsyncHandler = new CustomAsyncHandler(context.getContentResolver(), asyncQueryListener);
        }

        if (entry.exported) {
            exportVideoIcon.setVisibility(View.VISIBLE);
            exportVideoIcon.setImageResource(R.drawable.timeline_icn_download);
            return;
        }


        String where = CanaryVideoExportContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = {String.valueOf(entry.id)};
        customAsyncHandler.startQuery(DOWNLOAD_STATUS_QUERY, null,
                CanaryVideoExportContentProvider.CONTENT_URI, null, where, whereArgs, null);
    }

    public void setNotifiedIconVisibility(Entry entry) {
        String where = CanaryNotifiedContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = {String.valueOf(entry.id)};
        customAsyncHandler.startQuery(NOTIFIED_QUERY, null,
                CanaryNotifiedContentProvider.CONTENT_URI, null, where, whereArgs, null);
    }

    @Override
    public void setUpEntry(final Entry entry) {
        setUpEntryDescription(entry);
        setCommentIconVisibility(entry);
        setDownloadIconVisibility(entry);
        setNotifiedIconVisibility(entry);

        headerLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        dateTextView.setText(DateUtil.utcDateToDisplayString(entry.startTime));
        entryTitleTextView.setText(entry.description);
        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EntryDetailActivity.class);
                intent.putExtra(EntryDetailActivity.ADD_COMMENT, EntryDetailActivity.ADD_COMMENT);
                intent.putExtra("entryId", entry.id);
                context.startActivity(intent);
                if (context instanceof Activity) {
                    Activity activity = context;
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
                }
            }
        });

        getThumbnails(entry);

        showFlag(entry.starred);

        flaggedButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TouchTimeUtil.dontAllowTouch())
                            return;

                        if (Utils.isDemo()) {
                            AlertUtils.showGenericAlert(context, context.getString(R.string.save_video), context.getString(R.string.save_video_dsc));
                            return;
                        }


                        if (!context.hasInternetConnection()) {
                            return;
                        }

                        if (entry.starred) {

                            AlertUtils.showUnSaveWarningAlert(context, entry.startTime, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toggleFlag(entry);
                                }
                            });


                        } else {
                            toggleFlag(entry);
                        }
                    }
                });
    }

    private void toggleFlag(final Entry entry) {

        entry.starred = !entry.starred;

        if (entry.starred) {
            GoogleAnalyticsHelper.trackEntry(ACTION_SAVE, PROPERTY_TIMELINE, null, entry.getLocationId(), entry.id);
        } else {
            GoogleAnalyticsHelper.trackEntry(ACTION_UNSAVE, PROPERTY_TIMELINE, null, entry.getLocationId(), entry.id);
        }

        showFlag(entry.starred);

        EntryAPIService.setEntryRecordFlag(
                entry.id, entry.starred, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        // flagged patch was accepted on the server - now update the database asynchronously
                        EntryDatabaseService.setEntryAsFlagged(entry.id, entry.starred);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        try {
                            AlertUtils.showGenericAlert(context, Utils.getErrorMessageFromRetrofit(context, error));
                        } catch (JSONException ignored) {
                        }

                        entry.starred = !entry.starred;
                        showFlag(entry.starred);
                    }
                });
    }

    private void getThumbnails(Entry entry) {

        EntryThumbnailAdapter entryThumbnailAdapter = (EntryThumbnailAdapter) thumbnailViewPager.getAdapter();
        if (entryThumbnailAdapter.getCount() > 0) {
            thumbnailViewPager.setCurrentItem(0);
        }

        String deviceUUID = null;
        if (entry.displayMeta != null && entry.displayMeta.notified != null) {
            deviceUUID = entry.displayMeta.notified.deviceUUID;
        }
        entryThumbnailAdapter.swapThumbnails(entry.id, deviceUUID);

    }

    private void showFlag(boolean isFlagged) {
        if (isFlagged) {
            flaggedButton.setImageResource(R.drawable.ic_flag_active);
        } else {
            flaggedButton.setImageResource(R.drawable.ic_flag_inactive);
        }
    }
}
