package is.yranac.canary.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import is.yranac.canary.R;
import is.yranac.canary.ui.views.TextViewPlus;

public class WifiNetworkAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> arraylist;

    private String key;

    public WifiNetworkAdapter(Context context,
                              ArrayList<HashMap<String, String>> arraylist, String key) {
        super();
        inflater = LayoutInflater.from(context);
        this.arraylist = arraylist;
        this.key = key;
    }

    @Override
    public int getCount() {
        return this.arraylist.size() == 0 ? 0 : arraylist.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class WifiNetworkViewHolder {
        TextViewPlus titleTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        WifiNetworkViewHolder holder;
        if (view == null || !(view.getTag() instanceof WifiNetworkViewHolder)) {
            view = inflater.inflate(R.layout.setting_prompt, parent, false);
            holder = new WifiNetworkViewHolder();
            holder.titleTextView = (TextViewPlus) view
                    .findViewById(R.id.setting_title);
            holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
            holder = (WifiNetworkViewHolder) view.getTag();
        }

        if (position == getCount() - 1) {
            holder.titleTextView.setText(R.string.enter_network_manually);
            holder.titleTextView.setTextColor(inflater.getContext().getResources().getColor(R.color.dark_gray));
        } else {

            HashMap<String, String> wifiNetworkInfo = arraylist.get(position);
            holder.titleTextView.setText(wifiNetworkInfo.get(key));
            holder.titleTextView.setTextColor(inflater.getContext().getResources().getColor(R.color.black));
        }

        return view;
    }

}
