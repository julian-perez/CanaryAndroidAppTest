package is.yranac.canary.adapter;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanaryThumbnailContentProvider;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.EntryDetailActivity;
import is.yranac.canary.ui.views.ThumbnailImageView;
import is.yranac.canary.util.CustomAsyncHandler;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.ui.EntryDetailActivity.ENTRY_ID;
import static is.yranac.canary.ui.EntryDetailActivity.PLAYING_THUMBNAIL_ID;

/**
 * Created by Schroeder on 8/15/14.
 */
public class EntryThumbnailAdapter extends ViewPagerAdapter {
    private static final String LOG_TAG = "EntryThumbnailAdapter";

    private List<Thumbnail> thumbnailList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private long entryId;
    private BaseActivity context;
    private String notifiedDeviceUUID;

    private Stack<View> mRecycledViewsList;

    public EntryThumbnailAdapter(BaseActivity context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

        mRecycledViewsList = new Stack<>();
    }

    @Override
    public int getCount() {
        return thumbnailList.size();
    }

    @Override
    public View getView(final int position, ViewPager pager) {
        final View view = inflateOrRecycleView();

        final ThumbnailImageView imageView = (ThumbnailImageView) view.findViewById(R.id.thumbnail_image_view);
        imageView.setImageBitmap(null);


        if (thumbnailList.isEmpty()) {
            return view;
        }


        final Thumbnail thumbnail = thumbnailList.get(position);

        final String device = Utils.getStringFromResourceUri(thumbnail.device);

        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoogleAnalyticsHelper.trackEntry(AnalyticsConstants.ACTION_ENTRY_PLAY, AnalyticsConstants.PROPERTY_TIMELINE
                                , device, UserUtils.getLastViewedLocationId(), entryId);
                        Intent intent = new Intent(context, EntryDetailActivity.class);
                        intent.putExtra(ENTRY_ID, entryId);
                        intent.putExtra(PLAYING_THUMBNAIL_ID, thumbnail.id);
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                });

        imageView.loadThumbnail(thumbnail);

        return view;
    }

    private View inflateOrRecycleView() {
        final View viewToReturn;
        if (mRecycledViewsList.isEmpty()) {
            viewToReturn = layoutInflater.inflate(R.layout.entry_thumbnail, null, false);
        } else {
            viewToReturn = mRecycledViewsList.pop();
        }

        return viewToReturn;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        ViewPager pager = (ViewPager) container;
        View recycledView = (View) view;
        pager.removeView(recycledView);
        mRecycledViewsList.push(recycledView);
    }

    private final int THUMBNAIL_TOKEN = 3245;


    private boolean newEntry = false;
    private boolean thumbnailChanged = false;
    private boolean thumbnailQueryFinished;
    private CustomAsyncHandler.AsyncQueryListener asyncQueryListener = new CustomAsyncHandler.AsyncQueryListener() {
        @Override
        public void onQueryComplete(int token, Object cookie, Cursor cursor) {

            if (cursor == null)
                return;

            switch (token) {
                case THUMBNAIL_TOKEN:
                    setupThumbnails(cursor);
                    break;

            }
            cursor.close();


            if (thumbnailQueryFinished) {
                if (thumbnailChanged || newEntry)
                    notifyDataSetChanged();
            }

        }
    };


    private void setupThumbnails(Cursor cursor) {
        List<Thumbnail> thumbnails = new ArrayList<>();


        while (cursor.moveToNext()) {

            Thumbnail thumbnail = ThumbnailDatabaseService.getThumbnailFromCursor(cursor);
            String thumbnailDeviceUUID = Utils.getStringFromResourceUri(thumbnail.device);
            if (notifiedDeviceUUID != null && thumbnailDeviceUUID.equalsIgnoreCase(thumbnailDeviceUUID)) {
                thumbnails.add(0, thumbnail);
            } else {
                thumbnails.add(thumbnail);
            }
        }

        thumbnailQueryFinished = true;

        this.thumbnailChanged = this.thumbnailList.size() != thumbnails.size();
        this.thumbnailList = thumbnails;
    }


    private CustomAsyncHandler asyncHandler;

    public void swapThumbnails(long entryId, String deviceUUIDs) {

        this.notifiedDeviceUUID = deviceUUIDs;
        if (asyncHandler != null) {
            asyncHandler.cancelOperation(THUMBNAIL_TOKEN);
        } else {
            ContentResolver contentResolver = CanaryApplication.getContext()
                    .getContentResolver();
            asyncHandler = new CustomAsyncHandler(contentResolver, asyncQueryListener);
        }

        thumbnailQueryFinished = false;
        newEntry = this.entryId != entryId;
        this.entryId = entryId;


        String sortOrder = CanaryThumbnailContentProvider.COLUMN_DEVICE_ID + " ASC";


        String where2 = CanaryThumbnailContentProvider.COLUMN_ENTRY_ID + " == ?";
        String whereArgs2[] = {String.valueOf(entryId)};

        asyncHandler.startQuery(THUMBNAIL_TOKEN, null,
                CanaryThumbnailContentProvider.CONTENT_URI, null, where2, whereArgs2, sortOrder);


    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}