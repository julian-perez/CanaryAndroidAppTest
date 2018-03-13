package is.yranac.canary.util;

import android.content.Context;
import android.location.LocationManager;
import android.test.mock.MockContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by narendramanoharan on 5/24/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class LocationUtilTest {


    @Test
    public void testCanGetLocation() {

        Context context = mock(Context.class);

        LocationManager locationManager = mock(LocationManager.class);

        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);

        Mockito.when(context.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager);

        Boolean locationAssess = LocationUtil.canGetLocation(context);

        assertTrue(locationAssess);
    }
}