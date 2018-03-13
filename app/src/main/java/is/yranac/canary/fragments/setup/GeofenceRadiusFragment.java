package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsGeofenceRadiusBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.geofence.GeofenceUtils;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_GEOFENCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_GEOFENCE_SETTINGS_SIZE;

/**
 * Created by michaelschroeder on 5/9/17.
 */

public class GeofenceRadiusFragment extends SetUpBaseFragment implements OnMapReadyCallback, View.OnClickListener {


    private static final String LOG_TAG = "GeofenceRadiusFragment";
    private FragmentSettingsGeofenceRadiusBinding binding;
    private Location location;
    private int geofenceRadius;

    private static final int SMALL = 150;
    private static final int MEDIUM = 300;
    private static final int LARGE = 450;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsGeofenceRadiusBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);

        int locationId = getArguments().getInt(location_id);
        location = LocationDatabaseService.getLocationFromId(locationId);

        if (location == null)
            return;

        geofenceRadius = location.geofenceRadius;

        binding.mapView.getMapAsync(this);
        binding.smallLayout.setOnClickListener(this);
        binding.mediumLayout.setOnClickListener(this);
        binding.largeLayout.setOnClickListener(this);
        binding.saveBtn.setOnClickListener(this);


        binding.topLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        binding.setSizeTextView.setGravity(Gravity.CENTER);
        binding.setSizeDscTextView.setGravity(Gravity.CENTER);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View v;

                switch (geofenceRadius) {
                    case SMALL:
                        v = binding.smallLayout;
                        break;
                    case MEDIUM:
                        v = binding.mediumLayout;
                        break;
                    case LARGE:
                        v = binding.largeLayout;
                        break;
                    default:
                        return;
                }

                float setX = v.getX() + (v.getWidth() / 2) - (binding.selectedLevel.getWidth() / 2);

                binding.selectedLevel.animate().translationX(setX).start();
            }
        });


        binding.saveBtn.setEnabled(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
        fragmentStack.showHelpButton();
        fragmentStack.setHeaderTitle(R.string.geofence_size);
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
        binding.mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }


    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_GEOFENCE_SETTINGS_SIZE;
    }

    @Override
    public void onRightButtonClick() {

        GetHelpFragment fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_GEOFENCE);
        addModalFragment(fragment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.map_config));

        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        this.googleMap = googleMap;

        drawGeofence();
    }

    private void drawGeofence() {
        if (googleMap != null && location != null) {
            LatLng latLng = new LatLng(location.lat, location.lng);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_FACTOR));

            googleMap.clear();

            GeofenceUtils.changeOffsetCenter(binding.mapView, googleMap, latLng.latitude, latLng.longitude, ZOOM_FACTOR);
            GeofenceUtils.addMarker(getContext(), latLng, googleMap);
            GeofenceUtils.addGeofence(getContext(), latLng, googleMap, geofenceRadius);
        }
    }

    @Override
    public void onClick(View v) {
        float setX;
        switch (v.getId()) {
            case R.id.small_layout:
                setX = v.getX() + (v.getWidth() / 2) - (binding.selectedLevel.getWidth() / 2);
                binding.selectedLevel.animate().translationX(setX).start();
                geofenceRadius = SMALL;
                break;
            case R.id.medium_layout:
                geofenceRadius = MEDIUM;
                setX = v.getX() + (v.getWidth() / 2) - (binding.selectedLevel.getWidth() / 2);
                binding.selectedLevel.animate().translationX(setX).start();
                break;
            case R.id.large_layout:
                setX = v.getX() + (v.getWidth() / 2) - (binding.selectedLevel.getWidth() / 2);
                binding.selectedLevel.animate().translationX(setX).start();
                geofenceRadius = LARGE;
                break;
            case R.id.save_btn:
                save();
                break;

        }

        binding.mapView.getMapAsync(this);


    }

    private void save() {
        LocationAPIService.changeGeofenceRadius(getContext(), location, geofenceRadius);
        addFragmentToStack(AddADeviceFragment.newInstance(locationUri()), Utils.SLIDE_FROM_RIGHT);

    }
}
