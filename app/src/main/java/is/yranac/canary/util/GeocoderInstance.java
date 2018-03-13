package is.yranac.canary.util;

import android.content.Context;
import android.location.Geocoder;

public class GeocoderInstance {

    private static Geocoder geocoderInstance = null;

    public static Geocoder getInstance(Context context) {


        if (geocoderInstance == null) {
            geocoderInstance = new Geocoder(context);
        }

        return geocoderInstance;
    }

}
