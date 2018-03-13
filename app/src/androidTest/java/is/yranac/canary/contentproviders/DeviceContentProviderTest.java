package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.Date;

import is.yranac.canary.Constants;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.Utils;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class DeviceContentProviderTest extends ProviderTestCase2<CanaryDeviceContentProvider> {

    @Mock
    public Device device;

    @Mock
    Location mLocation;

    @Mock
    Cursor cursor;

    /**
     * Constructor.
     */
    public DeviceContentProviderTest() {
        super(CanaryDeviceContentProvider.class, Constants.AUTHORITY_DEVICE);


    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        cursor = mock(Cursor.class);

        mLocation = mock(Location.class);
        mLocation.id = 5000;
        mLocation.created = new Date();
        mLocation.resourceUri = Constants.LOCATIONS_URI + "5000/";

        device = mock(Device.class);
        device.activationStatus = "activated";
        device.ota_status = "complete";
        device.serialNumber = "fake";
        device.uuid = "fake";
        device.id = 5000;
        device.mode = Mode.ERROR_URI;
        device.location = mLocation.resourceUri;
        device.resourceUri = Constants.DEVICE_URI + "5000/";

        final ContentProvider contentProvider = new CanaryDeviceContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_DEVICE;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_DEVICE, contentProvider);

        final ContentProvider mContentProvider = new CanaryLocationContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);

        BaseDatabaseService.contentResolver = getMockContentResolver();
    }

    public void testInsert() {
        LocationDatabaseService.insertLocation(mLocation);
        DeviceDatabaseService.insertOrUpdateDevice(device);
    }

    public void testQuery() {
        Device device2 = DeviceDatabaseService.getDeviceFromId(device.id);
        assertNotNull(device2);
        assertEquals(device2.id, device.id);
    }

    public void testUpdate() {
        DeviceDatabaseService.updateDeviceName(String.valueOf(device.id), "test");
        Device device1 = DeviceDatabaseService.getDeviceFromId(device.id);
        assertNotNull(device1);
        assertEquals(device1.id, device.id);
        assertEquals(device1.name, "test");
    }

    public void testUpdateMode() throws Exception {
        Device device1 = DeviceDatabaseService.getDeviceFromId(device.id);
        assertNotNull(device1);
        assertEquals(device1.mode, Mode.ERROR_URI);
        DeviceDatabaseService.updateDeviceMode(device, 4);
        Device device2 = DeviceDatabaseService.getDeviceFromId(device.id);
        assertNotNull(device2);
        assertEquals(device2.mode, Utils.buildResourceUri(Constants.MODES_URI, 4));
    }

    public void testDelete() {
        DeviceDatabaseService.deleteDevice(device.id);
        Device device2 = DeviceDatabaseService.getDeviceFromId(device.id);
        assertNull(device2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(DeviceContentProviderTest.class);
    }

    public void testTODO() throws Exception {
        //TODO: Add More tests
    }
}
