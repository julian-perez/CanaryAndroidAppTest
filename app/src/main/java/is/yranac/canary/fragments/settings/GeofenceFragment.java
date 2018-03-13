package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsGeofenceBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.util.ColorUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_GEOFENCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_HELP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_GEOFECE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_GEOFENCE_SETTINGS;

/**
 * Created by michaelschroeder on 5/9/17.
 */

public class GeofenceFragment extends SettingsFragment implements OnMapReadyCallback, View.OnClickListener {


    private FragmentSettingsGeofenceBinding binding;
    private Location location;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsGeofenceBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);

        binding.editPosition.setOnClickListener(this);
        binding.editSize.setOnClickListener(this);

    }


    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        binding.mapView.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
        binding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
        int locationId = getArguments().getInt(location_id);
        TinyMessageBus.post(new GetLocation(locationId));
        fragmentStack.setHeaderTitle(R.string.geofence);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
        fragmentStack.resetButtonStyle();
    }

    @Override
    public void onStop() {
        super.onStop();
        TinyMessageBus.unregister(this);
        binding.mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        binding.mapView.onDestroy();
        super.onDestroy();
    }

    @Subscribe
    public void onGotLocation(GotLocationData gotLocationData) {
        location = gotLocationData.location;
        binding.mapView.getMapAsync(this);

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_GEOFENCE_SETTINGS;
    }


    @Override
    public void onRightButtonClick() {
        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_HELP, PROPERTY_GEOFECE, null, location.id, 0);

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
                .radius(location.geofenceRadius)
                .strokeColor(colorClear)
                .fillColor(colorGreen));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.edit_size:
                addFragmentToStack(getInstance(GeofenceRadiusFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.edit_position:
                addFragmentToStack(getInstance(GeofencePostionFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;
        }
    }
}
