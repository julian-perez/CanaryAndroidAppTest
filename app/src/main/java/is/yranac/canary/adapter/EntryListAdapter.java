package is.yranac.canary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import is.yranac.canary.R;
import is.yranac.canary.adapter.holder.EntryViewHolder;
import is.yranac.canary.adapter.holder.EntryViewHolderMotion;
import is.yranac.canary.adapter.holder.EntryViewHolderSimple;
import is.yranac.canary.adapter.holder.EntryViewholderHomehealth;
import is.yranac.canary.adapter.holder.SendFeedbackHolder;
import is.yranac.canary.contentproviders.CanaryEntryContentProvider;
import is.yranac.canary.databinding.ListrowSendFeedbackBinding;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.jobs.APIEntryJobService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_AIR_QUALITY;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_HUMIDITY;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_LIVE;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_MOTION;
import static is.yranac.canary.model.entry.Entry.ENTRY_TYPE_TEMPERATURE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TIMELINE_ENTRY_SCROLLED;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_TIMELINE;

public class EntryListAdapter extends CursorAdapter implements StickyListHeadersAdapter {
    public static final int ENTRY_TYPE_MOTION_CODE = 0;
    public static final int ENTRY_TYPE_SIMPLE_CODE = 1;
    public static final int ENTRY_TYPE_HOMEHEALTH_CODE = 2;
    public static final int SUBMIT_FEEDBACK_CODE = 3;

    public static final int ENTRY_TYPE_COUNT = 4;


    private static final String TAG = "EntryListAdapter";
    private static final String LOG_TAG = TAG;

    private LayoutInflater inflater;

    private BaseActivity activity;

    private int type;
    private boolean showAmazonHeader;

    public class HeaderViewHolder {
        public TextView dayTextView;
    }


    public EntryListAdapter(BaseActivity context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        activity = context;
        inflater = LayoutInflater.from(context);
    }

    private void locationHeaderView(EntryViewHolderMotion holder) {
        holder.headerLayout.setVisibility(View.VISIBLE);
        holder.mainLayout.setVisibility(View.GONE);
        switch (type) {
            case Entry.SHOWING_AWAY_MODE:
                holder.headerTextView.setText(R.string.away_events);
                break;
            case Entry.SHOWING_FLAGGED:
                holder.headerTextView.setText(R.string.bookmark_events);
                break;
            case Entry.SHOWING_ALL:
                holder.headerTextView.setText(R.string.all_events);
                break;
        }
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        EntryViewHolder viewHolder;
        switch (getItemViewType(cursor.getPosition())) {
            case ENTRY_TYPE_MOTION_CODE:
                viewHolder = (EntryViewHolderMotion) view.getTag();
                if (cursor.getPosition() == 0) {
                    locationHeaderView((EntryViewHolderMotion) viewHolder);
                    return;
                }
                break;

            case ENTRY_TYPE_HOMEHEALTH_CODE:
                viewHolder = (EntryViewholderHomehealth) view.getTag();
                break;

            case SUBMIT_FEEDBACK_CODE:
                SendFeedbackHolder holder = (SendFeedbackHolder) view.getTag();
                holder.checkLayout();
                return;
            default:
                viewHolder = (EntryViewHolderSimple) view.getTag();
                break;
        }

        Entry entry = EntryDatabaseService.getEntryFromCursor(cursor);
        viewHolder.setUpBackground(showAmazonHeader, cursor, entry);
        viewHolder.setUpEntry(entry);


        GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_ENTRY_SCROLLED,
                null, null, UserUtils.getLastViewedLocationId(), entry.id);

        // If the database is not being updated OR it is being updated but it's a refresh
        // ...and we are at or past the update threshold
        // ...and there is more data available on the server
        // ...and we have an internet connection
        // then request another fetch
        // we can request another fetch here even if we are getting the latest records because the intentservice will queue requests


        boolean isUpdating = APIEntryJobService.Status.INSTANCE.getIsUpdating();

        int thisRow = cursor.getPosition();
        int rowCount = cursor.getCount();
        int threshold = rowCount - (APIEntryJobService.BATCHSIZE - 1);
        if ((!isUpdating || (APIEntryJobService.Status.INSTANCE.getMode() == APIEntryJobService.GETLATEST)) &&
                (thisRow > threshold && !EntryDatabaseService.allValidRecordsAreInDatabase(UserUtils.getLastViewedLocationId(), type)
                        && Utils.hasInternetConnection(context))) {
            APIEntryJobService.fetchAllEntries(context, UserUtils.getLastViewedLocationId(), type);
        }
    }

    @Override
    public View newView(final Context context, Cursor cursor, final ViewGroup viewGroup) {

        switch (getItemViewType(cursor.getPosition())) {
            case ENTRY_TYPE_MOTION_CODE:
                View motionView = inflater.inflate(R.layout.listrow_entry_motion, viewGroup, false);
                final EntryViewHolderMotion motionHolder = new EntryViewHolderMotion(activity, motionView, viewGroup);
                motionView.setTag(motionHolder);
                return motionView;

            case ENTRY_TYPE_HOMEHEALTH_CODE:
                View homeHealthView = inflater.inflate(R.layout.listrow_entry_homehealth, viewGroup, false);
                final EntryViewholderHomehealth homehealthHOlder = new EntryViewholderHomehealth(activity, homeHealthView, viewGroup);
                homeHealthView.setTag(homehealthHOlder);
                return homeHealthView;

            case SUBMIT_FEEDBACK_CODE:
                ListrowSendFeedbackBinding listrowSendFeedbackBinding = ListrowSendFeedbackBinding.inflate(inflater);
                final SendFeedbackHolder sendFeedbackHolder = new SendFeedbackHolder(activity, listrowSendFeedbackBinding);
                listrowSendFeedbackBinding.getRoot().setTag(sendFeedbackHolder);
                return listrowSendFeedbackBinding.getRoot();

            default:
                View simpleView = inflater.inflate(R.layout.listrow_entry_simple, viewGroup, false);
                final EntryViewHolderSimple simpleHolder = new EntryViewHolderSimple(simpleView, activity);
                simpleView.setTag(simpleHolder);
                return simpleView;
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.listrow_entry_header, parent, false);
            holder.dayTextView = convertView.findViewById(R.id.header_text_view);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Cursor cursor = getCursor();
        if (position == 0)
            cursor.moveToPosition(position + (showAmazonHeader ? 2 : 1));
        else if (position == 1 && showAmazonHeader)
            cursor.moveToPosition(position + 1);
        else
            cursor.moveToPosition(position);

        long startTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_START_TIME));
        Date startDate = new Date(startTimeStamp);
        holder.dayTextView.setText(DateUtil.dateToDay(mContext, startDate));

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        if (position == 0)
            return ENTRY_TYPE_MOTION_CODE;

        if (position == 1 && showAmazonHeader) {
            return SUBMIT_FEEDBACK_CODE;
        }

        String type = cursor.getString(
                cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_ENTRY_TYPE));

        switch (type) {
            case ENTRY_TYPE_MOTION:
            case ENTRY_TYPE_LIVE:
                return ENTRY_TYPE_MOTION_CODE;
            case ENTRY_TYPE_AIR_QUALITY:
            case ENTRY_TYPE_HUMIDITY:
            case ENTRY_TYPE_TEMPERATURE:
                return ENTRY_TYPE_HOMEHEALTH_CODE;
            default:
                return ENTRY_TYPE_SIMPLE_CODE;
        }
    }


    @Override
    public int getViewTypeCount() {
        return ENTRY_TYPE_COUNT;
    }

    @Override
    public long getHeaderId(int position) {
        Cursor cursor = getCursor();

        int checkPosition = position;
        if (position == 0)
            checkPosition = showAmazonHeader ? 2 : 1;
        else if (position == 1 && showAmazonHeader)
            checkPosition = 2;

        if (cursor.moveToPosition(checkPosition)) {
            Date startTimeStamp = new Date(cursor.getLong(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_START_TIME)));
            return Utils.findDayForDate(startTimeStamp);
        }
        return 0;
    }


    public void swapCursor(int type, Cursor cursor, boolean showAmazonHeader) {
        this.showAmazonHeader = showAmazonHeader;
        this.type = type;
        super.changeCursor(cursor);
    }
}
