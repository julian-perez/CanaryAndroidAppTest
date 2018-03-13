package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.Date;

import is.yranac.canary.Constants;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.LocationDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 2/12/16.
 */
public class LocationContentProviderTest extends ProviderTestCase2<CanaryLocationContentProvider> {

    @Mock
    public Location location;

    /**
     * Constructor.
     */
    public LocationContentProviderTest() {
        super(CanaryLocationContentProvider.class, Constants.AUTHORITY_LOCATION);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        location = mock(Location.class);
        location.created = new Date();
        location.id = 666;
        location.name = "desk";
        location.resourceUri = Constants.LOCATIONS_URI + "666/";

        final ContentProvider mContentProvider = new CanaryLocationContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        MockContentResolver mMockContentResolver = getMockContentResolver();
        mMockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);
    }

    public void testInsert() {
        LocationDatabaseService.insertLocation(location);
    }

    public void testQuery() {
        Location location1 = LocationDatabaseService.getLocationFromId(location.id);
        assertNotNull(location1);
        assertEquals(location1.name, location.name);
    }

    public void testUpdate() {
        location.name = "newName";
        LocationDatabaseService.insertLocation(location);
        Location location1 = LocationDatabaseService.getLocationFromId(location.id);
        assertNotNull(location1);
        assertEquals(location1.name, "newName");
    }

    public void testDelete() {
        LocationDatabaseService.deleteLocation(location.id);
        Location location1 = LocationDatabaseService.getLocationFromId(location.id);
        assertNull(location1);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(LocationContentProviderTest.class);
    }

}
