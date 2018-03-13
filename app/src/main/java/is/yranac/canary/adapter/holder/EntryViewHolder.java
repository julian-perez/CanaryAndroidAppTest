package is.yranac.canary.adapter.holder;

import android.database.Cursor;
import android.view.View;

import java.security.InvalidParameterException;

import is.yranac.canary.R;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.Utils;

/**
 * Created by sergeymorozov on 3/25/16.
 */
public abstract class EntryViewHolder {
    protected BaseActivity context;

    protected View bottomPadTerminator;
    protected View topPadTerminator;

    public abstract void setUpEntry(Entry entry);

    public EntryViewHolder(View view, BaseActivity context) {
        this.context = context;
        this.topPadTerminator = view.findViewById(R.id.top_pad_terminator);
        this.bottomPadTerminator = view.findViewById(R.id.bottom_pad_terminator);

        if (topPadTerminator == null || bottomPadTerminator == null)
            throw new InvalidParameterException("View that was passed into EntryViewHolder " +
                    "doesn't have proper page terminators.");
    }

    public void setUpBackground(boolean showAmazonHeader, Cursor cursor, Entry entry) {

        int position = cursor.getPosition();

        if (cursor.moveToPosition(position + 1)) {
            Entry nextEntry = EntryDatabaseService.getEntryFromCursor(cursor);

            if (Utils.findDayForDate(entry.startTime) == Utils.findDayForDate(nextEntry.startTime)) {
                bottomPadTerminator.setVisibility(View.INVISIBLE);
            } else {
                bottomPadTerminator.setVisibility(View.VISIBLE);
            }
        } else {
            bottomPadTerminator.setVisibility(View.VISIBLE);
        }

        if (position == (showAmazonHeader ? 2 : 1)) {
            topPadTerminator.setVisibility(View.INVISIBLE);
        } else if (cursor.moveToPosition(position - 1)) {
            Entry previousEntry = EntryDatabaseService.getEntryFromCursor(cursor);

            if (Utils.findDayForDate(entry.startTime) == Utils.findDayForDate(previousEntry.startTime)) {
                topPadTerminator.setVisibility(View.INVISIBLE);
            } else {
                topPadTerminator.setVisibility(View.VISIBLE);
            }
        } else {
            topPadTerminator.setVisibility(View.VISIBLE);
        }
    }
}
