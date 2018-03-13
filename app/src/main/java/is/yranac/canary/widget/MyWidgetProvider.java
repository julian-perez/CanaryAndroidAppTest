package is.yranac.canary.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RemoteViews;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.messages.LocationAndEntry;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.StringUtils;

/**
 * Created by Schroeder on 1/4/16.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = "MyWidgetProvider";

    /**
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     **/

    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId,
                    remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {

        Log.i(LOG_TAG, "appWidgetId  " + appWidgetId);
        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.example_appwidget);

        List<LocationAndEntry> locationWithEntries = EntryDatabaseService.getLatestEntry();

        if (locationWithEntries.size() > 0) {
            LocationAndEntry locationWithEntry = locationWithEntries.get(0);
            remoteViews.setTextViewText(R.id.location_name, locationWithEntry.location.name);
            remoteViews.setTextViewText(R.id.text1, locationWithEntry.entry.description);
            List<Thumbnail> thumbnails = locationWithEntry.entry.thumbnails;
            if (thumbnails.size() != 0) {
                Thumbnail firstThumbnail = thumbnails.get(0);
                getImage(remoteViews, firstThumbnail.imageUrl(), context, appWidgetId);
                Intent inet = new Intent(context, LaunchActivity.class);
                inet.putExtra("entryId", locationWithEntry.entry.id);
                inet.setAction("entry");
                PendingIntent pIntentNetworkInfo = PendingIntent.getActivity(context, 2,
                        inet, PendingIntent.FLAG_ONE_SHOT);
                remoteViews.setOnClickPendingIntent(R.id.image_view, pIntentNetworkInfo);

            }


            for (Customer customer : locationWithEntry.location.customers){
                String location = StringUtils.isNullOrEmpty(customer.currentLocation) ? "unknown " : customer.currentLocation;
                Log.i(LOG_TAG, "customer location " + location);
            }
            setUpActionButtons(remoteViews, context);
        }
        return remoteViews;
    }

    private void setUpActionButtons(RemoteViews remoteViews, Context context) {

        Intent inet1 = new Intent(context, LaunchActivity.class);
        inet1.setAction("watchlive");
        PendingIntent pIntentNetworkInfo1 = PendingIntent.getActivity(context, 2,
                inet1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.watch_live_widget, pIntentNetworkInfo1);

        Intent inet2 = new Intent(context, LaunchActivity.class);
        inet2.setAction("timeline");
        PendingIntent pIntentNetworkInfo2 = PendingIntent.getActivity(context, 3,
                inet2, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.timeline_widget, pIntentNetworkInfo2);

        Intent inet3 = new Intent(context, LaunchActivity.class);
        inet3.setAction("bookmark");
        PendingIntent pIntentNetworkInfo3 = PendingIntent.getActivity(context, 4,
                inet3, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.bookedmark_widget, pIntentNetworkInfo3);
    }

    private void getImage(final RemoteViews remoteViews, String imageUrl, final Context context, final int appWidgetId) {
        ImageUtils.downloadUrl(imageUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                Log.i(LOG_TAG, "started image");

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                Log.e(LOG_TAG, "failed image", failReason.getCause());

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Log.i(LOG_TAG, "complete");
                remoteViews.setImageViewBitmap(R.id.image_view, bitmap);
                AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, remoteViews);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

                Log.e(LOG_TAG, "cancelled image");
            }
        });
    }
}