package is.yranac.canary.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.CountryCode;

import static is.yranac.canary.util.MapUtils.GEOCODE_TYPE.GEOCODE_TYPE_ADDRESS;
import static is.yranac.canary.util.MapUtils.GEOCODE_TYPE.GEOCODE_TYPE_LAT_LNG;

public class MapUtils {

    public enum GEOCODE_TYPE {
        GEOCODE_TYPE_LAT_LNG,
        GEOCODE_TYPE_ADDRESS
    }

    public interface AddressCallback {
        void onNewAddress(Address address, GEOCODE_TYPE geocode_type);
    }

    public static void geocodeLocationName(String locationName, Context context, AddressCallback addressCallback) {

        new GeocodingTask(context, addressCallback).execute(locationName);

    }


    private static class GeocodingTask extends AsyncTask<String, Void, Address> {

        private final AddressCallback addressCallback;
        private final Context context;

        public GeocodingTask(Context context, AddressCallback addressCallback) {
            this.addressCallback = addressCallback;
            this.context = context;
        }

        @Override
        protected Address doInBackground(String... params) {
            String locationName = params[0];
            Geocoder geoCoder = GeocoderInstance.getInstance(context);

            List<Address> matches = null;
            try {
                matches = geoCoder.getFromLocationName(locationName, 1);
            } catch (Exception ignore) {
            }
            if (matches != null && !matches.isEmpty()) {
                return matches.get(0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Address address) {
            if (address != null) {
                addressCallback.onNewAddress(address, GEOCODE_TYPE_ADDRESS);
            }
        }
    }

    public static void reverseGeocodeLatLng(Location location, Context context, AddressCallback addressCallback) {

        new ReverseGeocodingTask(context, addressCallback).execute(location);
    }

    private static class ReverseGeocodingTask extends AsyncTask<Location, Void, Address> {
        private Context context;
        private AddressCallback addressCallback;

        public ReverseGeocodingTask(Context context, AddressCallback addressCallback) {
            super();
            this.context = context;
            this.addressCallback = addressCallback;
        }

        @Override
        protected Address doInBackground(Location... locations) {
            Geocoder geoCoder = GeocoderInstance.getInstance(context);
            double latitude = locations[0].getLatitude();
            double longitude = locations[0].getLongitude();

            List<Address> addresses = new ArrayList<>();

            try {
                addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException ignored) {
            }

            if (addresses.size() == 0) {
                return null;
            }
            return addresses.get(0);
        }

        @Override
        protected void onPostExecute(Address address) {
            if (address != null) {
                addressCallback.onNewAddress(address, GEOCODE_TYPE_LAT_LNG);
            }
        }
    }


    public static String getStateAbbreviation(String adminArea, String countryName) {
        if (countryName == null || !countryName.equalsIgnoreCase("United States")) {
            return adminArea;
        }

        String jsonString = Utils.loadJSONFromRawFile(R.raw.states);
        Type listType = new TypeToken<ArrayList<CountryCode>>() {
        }.getType();
        final List<CountryCode> countryCodes = new Gson().fromJson(jsonString, listType);

        if (countryCodes == null)
            return adminArea;

        for (CountryCode countryCode : countryCodes) {
            if (countryCode.code.equalsIgnoreCase(adminArea) || countryCode.name.equalsIgnoreCase(adminArea))
                return countryCode.code;
        }

        return adminArea;

    }
}
