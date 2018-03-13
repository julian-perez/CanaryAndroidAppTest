package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import is.yranac.canary.Constants;
import is.yranac.canary.model.devicetoken.DeviceToken;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.DeviceTokenDatabaseService;

/**
 * Created by Schroeder on 1/9/15.
 */
public class DeviceTokenContentProviderTest extends ProviderTestCase2<CanaryDeviceTokenContentProvider> {

    @Mock
    public String device;

    /**
     * Constructor.
     */
    public DeviceTokenContentProviderTest() {
        super(CanaryDeviceTokenContentProvider.class, Constants.AUTHORITY_DEVICE);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        device = "jelfv";

        final ContentProvider contentProvider = new CanaryDeviceTokenContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_DEVICE_TOKEN;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_DEVICE_TOKEN, contentProvider);
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testInsert() {
        DeviceTokenDatabaseService.saveToken(device);
    }

    public void testQuery() {
        DeviceToken deviceToken = DeviceTokenDatabaseService.getCurrentToken();
        assert deviceToken != null;
        assertEquals(deviceToken.token, device);
    }

    public void testUpdate() throws Exception {
        DeviceToken deviceToken1 = DeviceTokenDatabaseService.getCurrentToken();
        device = "Beta";
        DeviceTokenDatabaseService.saveToken(device);
        DeviceToken deviceToken2 = DeviceTokenDatabaseService.getCurrentToken();
        assertNotNull(deviceToken1);
        assertNotNull(deviceToken2);
        assertNotSame(deviceToken1.token, deviceToken2.token);
    }

    public void testDelete() {
        DeviceTokenDatabaseService.deleteTokens();
        DeviceToken deviceToken = DeviceTokenDatabaseService.getCurrentToken();
        assertNull(deviceToken);
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(DeviceTokenContentProviderTest.class);
    }
}
