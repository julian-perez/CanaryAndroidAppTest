package is.yranac.canary.fragments.settings;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsGeofenceRadiusBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.ColorUtil;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_GEOFENCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_HELP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_GEOFECE_SIZE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_GEOFENCE_SETTINGS_SIZE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_GEOFENCE_SIZE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_LOCATION;

/**
 * Created by michaelschroeder on 5/9/17.
 */

public class GeofenceRadiusFragment extends SettingsFragment implements OnMapReadyCallback, View.OnClickListener {


    private static final String LOG_TAG = "GeofenceRadiusFragment";
    private FragmentSettingsGeofenceRadiusBinding binding;
    private Location location;
    private int geofenceRadius;

    private static final int SMALL = 150;
    private static final int MEDIUM = 300;
    private static final int LARGE = 450;

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
        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_HELP, PROPERTY_GEOFECE_SIZE, null, location.id, 0);

        is.yranac.canary.fragments.setup.GetHelpFragment fragment;
        fragment = is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(GET_HELP_TYPE_GEOFENCE);
        addModalFragment(fragment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.map_config));

        LatLng latLng = new LatLng(location.lat, location.lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_FACTOR));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);

        final int colorGreen = ColorUtil.darkModerateCyanThirty(getContext());
        final int colorClear = ColorUtil.transparent(getContext());
        googleMap.clear();
        googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(geofenceRadius)
                .strokeColor(colorClear)
                .fillColor(colorGreen));
        Bitmap icon = ImageUtils.getBitmapFromVectorDrawable(getContext(),
                R.drawable.ic_combined_shape);


        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));
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


        if (geofenceRadius == location.geofenceRadius) {
            binding.saveBtn.setEnabled(false);
        } else {
            binding.saveBtn.setEnabled(true);
        }
    }

    private void save() {
        GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_GEOFENCE_SIZE, SETTINGS_TYPE_LOCATION,
                0, null, location.id, String.valueOf(location.geofenceRadius), String.valueOf(geofenceRadius));
        LocationAPIService.changeGeofenceRadius(getContext(), location, geofenceRadius);
        getActivity().onBackPressed();
    }
}
