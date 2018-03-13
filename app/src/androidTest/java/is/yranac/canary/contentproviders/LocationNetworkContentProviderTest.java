package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.locationnetwork.LocationNetwork;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.LocationNetworkDatabaseService;
import is.yranac.canary.util.Utils;

import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 6/1/16.
 */
public class LocationNetworkContentProviderTest extends ProviderTestCase2<CanaryLocationNetworkContentProvider> {

    @Mock
    WifiInfo info;

    @Mock
    Cursor cursor;

    @Mock
    Location location;

    /**
     * Constructor.
     */
    public LocationNetworkContentProviderTest() {
        super(CanaryLocationNetworkContentProvider.class, Constants.AUTHORITY_LOCATION_NETWORK);
    }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        location = mock(Location.class);
        location.id = 2500;
        location.resourceUri = Constants.LOCATIONS_URI + "2500/";
        location.created = new Date();
        location.name = "desk";

        info = mock(WifiInfo.class);

        cursor = mock(Cursor.class);

        final ContentProvider contentProvider = new CanaryLocationContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_LOCATION;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, contentProvider);

        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testInsert() throws Exception {
        LocationDatabaseService.insertLocation(location);
        Location mLoc = LocationDatabaseService.getLocationFromId(2500);
        assertNotNull(mLoc);
        assertEquals(location.id, mLoc.id);

        LocationNetworkDatabaseService.insertNetworkForLocation(info, location.id);
    }

    public void testQuery() throws Exception {
        List<LocationNetwork> locNet =  LocationNetworkDatabaseService.locationNetworkForLocation(location.id);
        LocationNetwork mLocationNetwork = locNet.get(0);
        assertEquals(mLocationNetwork.macAddress, info.getMacAddress());
    }

    public void testDelete() throws Exception {
        LocationNetworkDatabaseService.deleteLocationNetworks();

        int mLocation = Utils.getIntFromResourceUri(location.resourceUri);
        List<LocationNetwork> locNet =  LocationNetworkDatabaseService.locationNetworkForLocation(mLocation);
        assertSame(locNet.size(),0);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(LocationNetworkContentProviderTest.class);
    }
}

