package is.yranac.canary.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.yranac.canary.R;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 11/24/15.
 */
public class BluetoothDeviceAdapter extends BaseAdapter {

    private static final String LOG_TAG = "BluetoothDeviceAdapter";
    private final Set<BluetoothDevice> bluetoothDevicesSet = new HashSet<>();
    private final List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private final Context context;

    public BluetoothDeviceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return bluetoothDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clear() {
        bluetoothDevicesSet.clear();
        bluetoothDevices.clear();
        notifyDataSetChanged();
    }

    public void add(BluetoothDevice device) {
        bluetoothDevicesSet.add(device);
        bluetoothDevices.clear();
        bluetoothDevices.addAll(bluetoothDevicesSet);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView deviceNameTextView;
        public ImageView canaryImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.setting_device_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.deviceNameTextView = convertView.findViewById(R.id.device_name_text_view);
            viewHolder.canaryImageView = convertView.findViewById(R.id.setting_image_view);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String name = bluetoothDevices.get(position).getName();
        viewHolder.deviceNameTextView.setText(name);

        DeviceType deviceType = Utils.getDeviceTypeFromDeviceSerialNumber(name);
        if (deviceType != null) {
            switch (deviceType.id) {
                case DeviceType.CANARY_AIO:
                    viewHolder.canaryImageView.setImageResource(R.drawable.device_aio_front);
                    break;
                case DeviceType.FLEX:
                    viewHolder.canaryImageView.setImageResource(R.drawable.device_flex_front);
                    break;
                case DeviceType.CANARY_VIEW:
                    viewHolder.canaryImageView.setImageResource(R.drawable.device_view_front);
                    break;
            }
        } else {
            viewHolder.canaryImageView.setImageResource(R.drawable.device_aio_front);
        }
        return convertView;
    }


}
