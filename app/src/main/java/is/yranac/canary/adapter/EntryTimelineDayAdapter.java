package is.yranac.canary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.holder.EntryGridViewHolder;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.ui.views.TextViewPlus;
import is.yranac.canary.ui.views.ThumbnailImageView;
import is.yranac.canary.util.DateUtil;

/**
 * Created by Schroeder on 7/6/15.
 */
public class EntryTimelineDayAdapter extends ArrayAdapter<Entry> {

    private static final String LOG_TAG = "EntryTimelineDayAdapter";
    private final LayoutInflater inflater;
    private int rowHeight = 0;

    public EntryTimelineDayAdapter(Context context, List<Entry> objects, int rowHeight) {
        super(context, 0, objects);
        this.inflater = LayoutInflater.from(context);
        this.rowHeight = rowHeight;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Entry entry = getItem(position);

        View view = convertView;
        EntryGridViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.gridrow_entry, parent, false);

            holder = new EntryGridViewHolder();
            holder.entryStartTime = (TextViewPlus) view
                    .findViewById(R.id.entry_start_time_label);
            holder.entryThumbnail = (ThumbnailImageView) view.findViewById(R.id.thumbnail_image_view);
            view.setTag(holder);
        } else {
            holder = (EntryGridViewHolder) view.getTag();
        }


        holder.entryStartTime.setText(DateUtil.utcDateToDisplayString(entry.startTime));

        if (!entry.thumbnails.isEmpty()) {
            holder.entryThumbnail.setImageBitmap(null);
            Thumbnail thumbnail = entry.thumbnails.get(0);
            holder.entryThumbnail.loadThumbnail(thumbnail);
        }

        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, rowHeight));

        return view;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        notifyDataSetChanged();
    }
}