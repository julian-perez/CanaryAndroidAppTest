package is.yranac.canary.fragments.setup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupCheckGeofenceBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.util.ColorUtil;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.geofence.GeofenceUtils;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_GEOFENCE;


/**
 * Created by michaelschroeder on 8/16/17.
 */

public class CheckGeofenceFragment extends SetUpBaseFragment implements OnMapReadyCallback, View.OnClickListener {

    private FragmentSetupCheckGeofenceBinding binding;
    private Location location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSetupCheckGeofenceBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);
        binding.looksGoodBtn.setOnClickListener(this);
        binding.needsEditingBtn.setOnClickListener(this);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_GEOFENCE);
        addModalFragment(fragment);
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
        int locationId = Utils.getIntFromResourceUri(locationUri());
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

        Bitmap icon = ImageUtils.getBitmapFromVectorDrawable(getContext(),
                R.drawable.ic_combined_shape);


        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));

        GeofenceUtils.changeOffsetCenter(binding.mapView, googleMap, latLng.latitude,
                latLng.longitude, ZOOM_FACTOR);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.needs_editing_btn:
                addFragmentToStack(getInstance(GeofencePostionFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.looks_good_btn:
                addFragmentToStack(AddADeviceFragment.newInstance(locationUri()), Utils.SLIDE_FROM_RIGHT);
                break;
        }
    }
}
