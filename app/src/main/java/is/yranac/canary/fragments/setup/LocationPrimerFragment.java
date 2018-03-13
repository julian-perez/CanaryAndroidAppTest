package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupLocationPrimerBinding;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 7/28/16.
 */
public class LocationPrimerFragment extends SetUpBaseFragment {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 67;

    private FragmentSetupLocationPrimerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupLocationPrimerBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentToStack(SetAddressFragment.newInstance(new Bundle(), true), Utils.SLIDE_FROM_RIGHT);
            }
        });

        binding.allowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        });
    }

    @Override
    public void onRightButtonClick() {

    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.location_services);
        fragmentStack.showRightButton(0);
        fragmentStack.showHeader(true);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.

                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());

                if (hasPermission) {
                    addFragmentToStack(SetAddressFragment.newInstance(new Bundle(), true), Utils.SLIDE_FROM_RIGHT);
                }

            }
        }

    }
}
