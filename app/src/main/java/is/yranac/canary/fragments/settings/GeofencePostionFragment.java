package is.yranac.canary.fragments.settings;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import is.yranac.canary.model.emergencycontacts.ForceUpdateEmergencyNumbers;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.intent.GooglePlacesAPIIntentServiceEmergencyNumbers;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import is.yranac.canary.util.geofence.GeofenceUtils;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_GEOFENCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_HELP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_GEOFENCE_POSITION;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_GEOFENCE_SETTINGS_POSITION;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_GEOFENCE_POSITION;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_LOCATION;

/**
 * Created by michaelschroeder on 5/9/17.
 */

public class GeofencePostionFragment extends SettingsFragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleMap.OnCameraMoveListener{


    private static final int REQUEST_CODE = 0x0023647234;

    private FragmentSettingsGeofencePositionBinding binding;
    private Location location;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private boolean showSyncPopup;

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


    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_GEOFENCE_SETTINGS_POSITION;
    }

    @Override
    public void onRightButtonClick() {

        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_HELP, PROPERTY_GEOFENCE_POSITION, null, location.id, 0);
        is.yranac.canary.fragments.setup.GetHelpFragment fragment;
        fragment = is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(GET_HELP_TYPE_GEOFENCE);
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

        ignoreMove = true;
        this.googleMap.setOnCameraMoveListener(this);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_FACTOR));

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

    private boolean ignoreMove;

    @Override
    public void onLocationChanged(android.location.Location location) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LatLng latLng = new LatLng(lat, lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        checkLatLng();
    }

    @Override
    public void onCameraMove() {

        if (ignoreMove) {
            ignoreMove = false;
            return;
        }

        checkLatLng();

    }

    @Subscribe
    public void onForceUpdateEmergencyNumbers(ForceUpdateEmergencyNumbers replaceEmergencyNumbers) {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        Activity activity = getActivity();
        if (activity != null)
            activity.onBackPressed();

    }


    private void checkLatLng() {
        LatLng latLng = googleMap.getCameraPosition().target;

        if (latLng.equals(location.getLatLng())) {
            binding.saveBtn.setEnabled(false);
        } else {
            binding.saveBtn.setEnabled(true);
        }

        double distance = GeofenceUtils.distance(latLng.latitude, latLng.longitude,
                location.getLatLng().latitude, location.getLatLng().longitude);

        showSyncPopup = distance > 500.0d;
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
        LatLng latLng = googleMap.getCameraPosition().target;
        LocationAPIService.changeGeofencePosition(getContext(), location, latLng.latitude, latLng.longitude);
        GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_GEOFENCE_POSITION, SETTINGS_TYPE_LOCATION,
                0, null, location.id, null, null);
        if (showSyncPopup) {
            Activity activity = getActivity();
            if (activity == null)
                return;
            AlertUtils.showNewGenericAlert(activity, getString(R.string.update_location_contacts),
                    activity.getString(R.string.update), activity.getString(R.string.no),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GooglePlacesAPIIntentServiceEmergencyNumbers.getNewEmergencyNumbersForLocations(location.id, true);
                            mDialog = AlertUtils.initLoadingDialog(getActivity(), getActivity().getString(R.string.syncing));
                            mDialog.show();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            getActivity().onBackPressed();
                        }
                    });
        } else {
            getActivity().onBackPressed();
        }
    }
}
