package is.yranac.canary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.fragments.tutorials.masking.SetupDeviceMasksFragment.DeviceWithMask;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;

/**
 * Created by michaelschroeder on 2/17/17.
 */

public class DeviceWithMaskAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private final List<DeviceWithMask> deviceWithMasks = new ArrayList<>();

    private int layout;

    public DeviceWithMaskAdapter(Context context, int layout) {
        super();

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return deviceWithMasks.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceWithMasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class SettingHolder {
        TextView titleTextView;
        ImageView settingsIconImageView;
        TextView detailTextView;
        View contentView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
            holder = new SettingHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.location_title_text_view);
            holder.settingsIconImageView = (ImageView) convertView.findViewById(R.id.settings_icon_image_view);
            holder.detailTextView = (TextView) convertView.findViewById(R.id.location_detail_text_view);
            holder.contentView = convertView.findViewById(R.id.content_view);
            convertView.setTag(holder);
        } else {
            holder = (SettingHolder) convertView.getTag();
        }

        DeviceWithMask deviceWithMask = deviceWithMasks.get(position);
        holder.titleTextView.setText(deviceWithMask.device.name);

        int maskCount = deviceWithMask.deviceMasks.deviceMasks.size();
        String descriptionText;

        if (deviceWithMask.device.getDeviceType() == DeviceType.FLEX) {
            descriptionText = context.getString(R.string.unavailable_for_flex);
            holder.contentView.setAlpha(0.5f);
        } else if (maskCount == 1) {
            descriptionText = context.getString(R.string.mask_applied, maskCount);
            holder.contentView.setAlpha(1.0f);
        } else {
            descriptionText = context.getString(R.string.masks_applied, maskCount);
            holder.contentView.setAlpha(1.0f);
        }

        holder.detailTextView.setText(descriptionText);

        holder.settingsIconImageView.setImageResource(deviceWithMask.device.getIcon());


        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {

        DeviceWithMask mask = deviceWithMasks.get(position);

        return mask.device.isMaskCompatible();
    }

    public void swapMasks(List<DeviceWithMask> deviceWithMasks) {
        this.deviceWithMasks.clear();
        this.deviceWithMasks.addAll(deviceWithMasks);
        notifyDataSetChanged();
    }
}
