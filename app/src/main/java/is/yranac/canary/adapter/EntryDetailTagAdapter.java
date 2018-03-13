package is.yranac.canary.adapter;

import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.fragments.EntryDetailTagFragment;

/**
 * Created by Nick on 11/7/14.
 */
public class EntryDetailTagAdapter extends BaseAdapter {
    private static final String LOG_TAG = "EntryDetailTagAdapter";

    private List<EntryDetailTagFragment.Tag> tags;

    private LayoutInflater inflater;
    private Context context;

    public EntryDetailTagAdapter(Context context, List<EntryDetailTagFragment.Tag> tags) {
        this.context = context;
        this.tags = tags;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public EntryDetailTagFragment.Tag getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public void remove(int position) {
        tags.remove(position);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView cvTextView;
        public ImageView cvCheckBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = inflater.inflate(R.layout.listrow_cv_tag, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.cvCheckBox = (ImageView) convertView.findViewById(R.id.cv_tag_checkbox);
            viewHolder.cvTextView = (TextView) convertView.findViewById(R.id.cv_tag_text_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cvTextView.setText(tags.get(position).title);

        if (tags.get(position).selected) {
            viewHolder.cvCheckBox.setVisibility(View.VISIBLE);
            viewHolder.cvTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_moderate_cyan));
        } else {
            viewHolder.cvCheckBox.setVisibility(View.INVISIBLE);
            viewHolder.cvTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        return convertView;
    }
}
