package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import is.yranac.canary.Constants;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.util.Utils;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class AvatarContentProviderTest extends ProviderTestCase2<CanaryAvatarContentProvider> {

    @Mock
    public Avatar avatar;

    @Mock
    public Cursor cursor;

    @Mock
    Customer customer;

    /**
     * Constructor.
     */
    public AvatarContentProviderTest() {
        super(CanaryAvatarContentProvider.class, Constants.AUTHORITY_AVATAR);
    }

    @Override
    public void setUp() throws Exception {

        super.setUp();

        customer = mock(Customer.class);
        customer.id = 10001;
        customer.email = "fake@gmail.com";
        customer.firstName = "First";
        customer.lastName = "Last";
        customer.phone = "1234567890";
        customer.username = "efwe";
        customer.currentLocation = Constants.LOCATIONS_URI + "10001/";
        customer.resourceUri = Constants.CUSTOMER_URI + "10001/";
        customer.languagePreference = "eng";

        avatar = mock(Avatar.class);
        avatar.customer = customer.resourceUri;
        avatar.resourceUri = Constants.AVATAR_URI + "10001/";
        avatar.image = "image";
        avatar.id = 10001;
        avatar.thumbnailUrl = "http://fake.com/url";

        cursor = mock(Cursor.class);

        final ContentProvider mContentProvider = new CanaryCustomerContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_CUSTOMER;
        mContentProvider.attachInfo(getMockContext(), providerInfo);
        mContentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_CUSTOMER, mContentProvider);

        final ContentProvider xContentProvider = new CanaryAvatarContentProvider();
        ProviderInfo xProviderInfo = new ProviderInfo();
        xProviderInfo.authority = Constants.AUTHORITY_AVATAR;
        xContentProvider.attachInfo(getMockContext(), xProviderInfo);
        xContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_AVATAR, xContentProvider);
        BaseDatabaseService.contentResolver = getMockContentResolver();
    }

    public void testCreateContentValues() throws Exception {
        ContentValues content = AvatarDatabaseService.createContentValuesFromAvatar(avatar);
        assertNotNull(content);
        assertEquals(content.get("customer_id"), customer.id);
    }

    public void testInsert() {
        CustomerDatabaseService.insertOrUpdateCustomer(customer);
        AvatarDatabaseService.insertAvatar(avatar);
        assertNotNull(avatar);
    }

    public void testQuery() {
        Avatar mAvatar = AvatarDatabaseService.getAvatarFromCursor(cursor);
        assertNotNull(mAvatar);
        Avatar avatar1 = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);
        assertEquals(avatar.thumbnailUrl, avatar1.thumbnailUrl);
    }

    public void testUpdate() {
        AvatarDatabaseService.insertAvatar(avatar);
        int customerId = Utils.getIntFromResourceUri(avatar.customer);
        Avatar avatar1 = AvatarDatabaseService.getAvatarFromCustomerId(customerId);
        assert avatar1 != null;
        avatar.thumbnailUrl = "http://fake.com/durl";
        AvatarDatabaseService.insertAvatar(avatar);
        Avatar avatar2 = AvatarDatabaseService.getAvatarFromCustomerId(customerId);
        assert avatar2 != null;
        assertEquals(avatar1.customer, avatar2.customer);
        assertEquals(avatar1.id, avatar2.id);
        assertNotSame(avatar1.thumbnailUrl, avatar2.thumbnailUrl);
    }

    public void testDelete() {

        int customerId = Utils.getIntFromResourceUri(avatar.customer);
        AvatarDatabaseService.deleteAvatar(customerId);
        Avatar avatar2 = AvatarDatabaseService.getAvatarFromCustomerId(customerId);
        assertNull(avatar2);
    }

    @Override
    protected void tearDown() throws Exception {
        scrubClass(AvatarContentProviderTest.class);
        super.tearDown();
    }
}
