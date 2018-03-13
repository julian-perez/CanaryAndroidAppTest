package is.yranac.canary.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.yranac.canary.fragments.DeviceFragment;
import is.yranac.canary.messages.TutorialRequest;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TutorialUtil;

public class DeviceFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String LOG_TAG = "DeviceFragmentPagerAdapter";

    private Map<String, DeviceFragment> mPageReferences = new HashMap<>();

    private List<Device> deviceList = new ArrayList<>();

    public DeviceFragmentPagerAdapter(FragmentManager fm, List<Device> deviceList) {
        super(fm);

        this.deviceList = deviceList;
    }

    @Override
    public DeviceFragment getItem(int position) {
        Device device = deviceList.get(position);


        DeviceFragment deviceFragment;
        boolean canDisplaySecondDeviceTutorial = position == 0 && deviceList.size() == 2;
        if (mPageReferences.containsKey(device.serialNumber + position)) {
            deviceFragment = mPageReferences.get(device.serialNumber + position);
        } else {
            deviceFragment = DeviceFragment.newInstance(JSONUtil.getJSONString(device), position);
            mPageReferences.put(device.serialNumber + position, deviceFragment);
        }

        if (deviceList.size() > 1 && position == 0) {
            if (!TutorialUtil.isTutorialInProgress())
                TinyMessageBus.post(new TutorialRequest());
        }

        return deviceFragment;
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    public boolean containsDevice(String deviceId) {
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).serialNumber.equals(deviceId)) {
                return true;
            }
        }

        return false;
    }


}