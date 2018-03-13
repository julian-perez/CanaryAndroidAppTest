package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsGeofencePositionBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.geofence.GeofenceUtils;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_GEOFENCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_GEOFENCE_SETTINGS_POSITION;

/**
 * Created by michaelschroeder on 5/9/17.
 */

public class GeofencePostionFragment extends SetUpBaseFragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleMap.OnCameraIdleListener {


    private static final int REQUEST_CODE = 0x0023647234;

    private FragmentSettingsGeofencePositionBinding binding;
    private Location location;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsGeofencePositionBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);

        binding.saveBtn.setOnClickListener(this);
        binding.mapLocateBtn.setOnClickListener(this);
        binding.mapStyleBtn.setOnClickListener(this);

        binding.topLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        binding.setPostionTextView.setGravity(Gravity.CENTER);
        binding.setPostionDscTextView.setGravity(Gravity.CENTER);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_GEOFENCE_SETTINGS_POSITION;
    }

    @Override
    public void onRightButtonClick() {

        GetHelpFragment fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_GEOFENCE);
        addModalFragment(fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.geofence_position);
        binding.mapView.onResume();
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
        binding.mapView.onPause();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);

        int locationId = getArguments().getInt(location_id);
        TinyMessageBus.post(new GetLocation(locationId));
    }


    @Subscribe
    public void onGotLocation(GotLocationData gotLocationData) {
        location = gotLocationData.location;
        binding.mapView.getMapAsync(this);

    }

    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.map_config));
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(location.lat, location.lng);

        this.googleMap.setOnCameraIdleListener(this);
        binding.saveBtn.setEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_FACTOR));

        googleMap.clear();

        GeofenceUtils.addMarker(getContext(), latLng, googleMap);
    }

    @Override
    public void onClick(View v) {

        if (location == null || googleMap == null)
            return;

        switch (v.getId()) {
            case R.id.map_style_btn:
                int mapType = googleMap.getMapType();
                if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    binding.mapStyleBtn.setImageResource(R.drawable.map_terrain);
                } else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    binding.mapStyleBtn.setImageResource(R.drawable.map_grid);
                }
                break;
            case R.id.map_locate_btn:
                locateUser();
                break;
            case R.id.save_btn:
                save();
                break;
        }
    }

    private void locateUser() {
        if (googleApiClient == null) {
            buildGoogleApiClient();
            return;
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locateUser();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LatLng latLng = new LatLng(lat, lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        GeofenceUtils.changeOffsetCenter(binding.mapView, googleMap, lat, lng, ZOOM_FACTOR);
        googleMap.clear();
        GeofenceUtils.addMarker(getContext(), latLng, googleMap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.

                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());

                if (hasPermission) {
                    locateUser();
                }

            }

        }
    }

    private void save() {
        LatLng latLng = GeofenceUtils.getOffsetCenter(binding.mapView, googleMap);
        LocationAPIService.changeGeofencePosition(getContext(), location, latLng.latitude, latLng.longitude);

        addFragmentToStack(getInstance(GeofenceRadiusFragment.class), Utils.SLIDE_FROM_RIGHT);
    }

    @Override
    public void onCameraIdle() {
        googleMap.clear();


        GeofenceUtils.addMarker(getContext(), GeofenceUtils.getOffsetCenter(binding.mapView, googleMap), googleMap);

    }
}
