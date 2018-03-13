package is.yranac.canary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.ui.views.CustomRadioLocationView;

/**
 * Created by Schroeder on 7/18/16.
 */
public class SelectionDeviceLocationAdapter extends ArrayAdapter<Location> {

    private static final String LOG_TAG = "SelectionDeviceLocationAdapter";

    public SelectionDeviceLocationAdapter(Context context, List<Location> locations, int resource) {
        super(context, resource, locations);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.settings_row_location_radio, parent, false);
        }
        CustomRadioLocationView cV = (CustomRadioLocationView) convertView;
        cV.getTitle().setText(getItem(position).address);
        String name = getItem(position).name;
        if (TextUtils.isEmpty(name)){
            cV.getName().setVisibility(View.GONE);
        } else {
            cV.getName().setText(name);
            cV.getName().setVisibility(View.VISIBLE);
        }


        return cV;
    }
}