package is.yranac.canary.fragments.tutorials.masking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.DeviceWithMaskAdapter;
import is.yranac.canary.databinding.FragmentTutorialSetupMasksBinding;
import is.yranac.canary.fragments.settings.SettingsFragment;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.ui.MaskingActivity;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.ui.BaseActivity.key_device;
import static is.yranac.canary.ui.BaseActivity.key_masks;
import static is.yranac.canary.ui.BaseActivity.key_thumbnail;
import static is.yranac.canary.ui.BaseActivity.key_thumbnail_id;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_ONBOARDING_HELP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MASKING_ONBOARDING;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MASKING_ONBOARDING_MASKING;

/**
 * Created by michaelschroeder on 2/17/17.
 */

public class SetupDeviceMasksFragment extends SettingsFragment {

    private FragmentTutorialSetupMasksBinding binding;
    private int locationId;

    private DeviceWithMaskAdapter adapter;


    public static class DeviceWithMask {
        public Device device;
        public DeviceMasks deviceMasks;
    }


    public static SetupDeviceMasksFragment newInstance(int locationId) {
        SetupDeviceMasksFragment fragment = new SetupDeviceMasksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("locationId", locationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialSetupMasksBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt("locationId");

        adapter = new DeviceWithMaskAdapter(getContext(), R.layout.listrow_location_light);

        binding.headerLayoutTextView.setText(R.string.mask_areas_that_canary_should_ignore);
        binding.deviceMaskList.setAdapter(adapter);

        final WeakReference<SetupDeviceMasksFragment> fragmentWeakReference = new WeakReference<>(this);

        binding.deviceMaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceWithMask deviceWithMask = (DeviceWithMask) adapter.getItem(position);
                Thumbnail thumbnail = ThumbnailDatabaseService.getThumbnailsForDevice(deviceWithMask.device);
                Intent maskingActivity = new Intent(fragmentWeakReference.get().getContext(), MaskingActivity.class);
                Bundle extras = new Bundle();
                extras.putString(key_device, JSONUtil.getJSONString(deviceWithMask.device));
                if (thumbnail != null) {
                    extras.putString(key_thumbnail, thumbnail.imageUrl());
                    extras.putLong(key_thumbnail_id, thumbnail.id);
                }
                extras.putString(key_masks, JSONUtil.getJSONString(deviceWithMask.deviceMasks));

                maskingActivity.putExtras(extras);

                fragmentWeakReference.get().startActivity(maskingActivity);
            }
        });

        binding.helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_MASKING);
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING_ONBOARDING, ACTION_MASKING_ONBOARDING_HELP, PROPERTY_MASKING_ONBOARDING_MASKING, null, locationId, 0);
                fragmentWeakReference.get().addModalFragment(fragment);
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CVTutorialOverFragment fragment = CVTutorialOverFragment.newInstance(locationId);
                fragmentWeakReference.get().addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        List<Device> deviceList = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);
        List<DeviceWithMask> deviceWithMasks = new ArrayList<>();


        Collections.sort(deviceList, new Comparator<Device>() {
            @Override
            public int compare(Device o1, Device o2) {

                int deviceType1 = o1.getDeviceType();
                int deviceType2 = o2.getDeviceType();

                if (deviceType1 == deviceType2){
                    return 0;
                }

                if (deviceType1 == DeviceType.CANARY_AIO){
                    return -1;
                }

                return 1;
            }
        });

        for (Device device : deviceList) {
            DeviceWithMask deviceWithMask = new DeviceWithMask();
            String existingMasksJSON = PreferencesUtils.getDeviceMasks(device.id);
            deviceWithMask.deviceMasks = JSONUtil.getObject(existingMasksJSON, DeviceMasks.class);
            deviceWithMask.device = device;
            if (deviceWithMask.deviceMasks == null) {
                deviceWithMask.deviceMasks = new DeviceMasks();
            }
            deviceWithMasks.add(deviceWithMask);
        }
        adapter.swapMasks(deviceWithMasks);
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }
}
