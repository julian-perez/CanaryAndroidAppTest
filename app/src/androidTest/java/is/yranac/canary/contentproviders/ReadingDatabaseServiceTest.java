package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;

import is.yranac.canary.Constants;
import is.yranac.canary.model.SensorType;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 6/1/16.
 */
public class ReadingDatabaseServiceTest extends ProviderTestCase2<CanaryReadingContentProvider> {

    @Mock
    Device device;

    @Mock
    Reading reading;

    @Mock
    Location location;

    @Mock
    SensorType sensorType;

    /**
     * Constructor.
     */
    public ReadingDatabaseServiceTest() {
        super(CanaryReadingContentProvider.class, Constants.AUTHORITY_READING);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        device = new Device();
        device.id = 5500;
        device.location = Constants.LOCATIONS_URI + "5500/";
        device.resourceUri = Constants.DEVICE_URI + "5500/";
        device.activationStatus = "activated";
        device.ota_status = "complete";
        device.serialNumber = "fake";
        device.uuid = "5500";

        sensorType = mock(SensorType.class);
        sensorType.id = 5500;

        location = new Location();
        location.id = 5500;
        location.resourceUri = Constants.LOCATIONS_URI + "5500/";
        location.created = new Date();

        reading = new Reading();

        reading.deviceUri = device.resourceUri;
        reading.created = new Date();
        reading.sensorType = sensorType;

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
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testInsert() throws Exception {
        LocationDatabaseService.insertLocation(location);
        Location mLocation = LocationDatabaseService.getLocationFromId(5500);
        assertNotNull(mLocation);
        assertEquals(location.id, mLocation.id);

        DeviceDatabaseService.insertOrUpdateDevice(device);
        Device mDevice = DeviceDatabaseService.getDeviceFromId(5500);
        assertNotNull(mDevice);

        ArrayList<Reading> readings = new ArrayList<>();
        readings.add(reading);
        ReadingDatabaseService.insertReadings(readings);
    }

    public void testReadingByDevice() throws Exception {
        Reading mReading = ReadingDatabaseService.getFirstReadingByDevice(device.resourceUri);
        assertNotNull(mReading);
    }

    public void testDelete() throws Exception {
        ReadingDatabaseService.deleteReadings();
        Reading mReading = ReadingDatabaseService.getFirstReadingByDevice(device.resourceUri);
        assertNull(mReading);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(ReadingDatabaseServiceTest.class);
    }
}

