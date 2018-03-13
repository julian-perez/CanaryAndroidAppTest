package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.geofence.CacheGeofenceContentProvider;
import is.yranac.canary.model.locationchange.ClientLocation;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.CacheGeofenceDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class CacheGeofenceContentProviderTest extends ProviderTestCase2<CacheGeofenceContentProvider> {

    @Mock
    public ClientLocation clientLocation;

    @Mock
    Cursor cursor;

    /**
     * Constructor.
     */
    public CacheGeofenceContentProviderTest() {
        super(CacheGeofenceContentProvider.class, Constants.AUTHORITY_CACHE_GEOFENCE);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        clientLocation = mock(ClientLocation.class);
        clientLocation.type = "slide";
        clientLocation.uuid = "Alpha";
        clientLocation.id = 0;

        cursor = mock(Cursor.class);

        final ContentProvider contentProvider = new CacheGeofenceContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_CACHE_GEOFENCE;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_CACHE_GEOFENCE, contentProvider);
        BaseDatabaseService.contentResolver = getMockContentResolver();

    }

    public void testContentValues() throws Exception {
        ContentValues content = CacheGeofenceDatabaseService.createContentValuesFromClientLocation(clientLocation);
        assertEquals(content.get("uuid"), "Alpha");
    }

    public void testInsert() {
        CacheGeofenceDatabaseService.insertClientLocation(clientLocation);
    }
    
    public void testQuery() {
        ClientLocation client = CacheGeofenceDatabaseService.getClientLocationFromCursor(cursor);
        assertNotNull(client);
        List<ClientLocation> clientLocations = CacheGeofenceDatabaseService.getAllCacheClientLocations(0, 10);
        assertNotNull(clientLocations);
        ClientLocation mClientLocation = clientLocations.get(0);
        assertEquals(mClientLocation.uuid, clientLocation.uuid);
    }


    public void testDelete() {
        CacheGeofenceDatabaseService.deleteClientLocation(clientLocation);
        List<ClientLocation> clientLocations = CacheGeofenceDatabaseService.getAllCacheClientLocations(1,1);
        if(!clientLocations.isEmpty()) {
            ClientLocation xClientLocation = clientLocations.get(0);
            assertNotSame(xClientLocation.uuid, clientLocation.uuid);
        } else {
            assertEquals(clientLocations.size(), 0);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(CacheGeofenceContentProviderTest.class);

    }
}
