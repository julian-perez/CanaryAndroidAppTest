package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import is.yranac.canary.Constants;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class CustomerContentProviderTest extends ProviderTestCase2<CanaryCustomerContentProvider> {

    @Mock
    public Customer customer;

    @Mock
    Avatar avatar;

    /**
     * Constructor.
     */
    public CustomerContentProviderTest() {
        super(CanaryCustomerContentProvider.class, Constants.AUTHORITY_CUSTOMER);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        avatar = mock(Avatar.class);
        avatar.customer = Constants.CUSTOMER_URI + "4000/";
        avatar.resourceUri = Constants.AVATAR_URI + "4000/";
        avatar.thumbnailUrl = "www.fake.com/sale";
        avatar.id = 4000;
        avatar.image = "picture";

        customer = mock(Customer.class);
        customer.email = "fake@gmail.com";
        customer.firstName = "First";
        customer.lastName = "Last";
        customer.phone = "1234567890";
        customer.username = "naren";
        customer.languagePreference = "eng";
        customer.resourceUri = Constants.CUSTOMER_URI + "4000/";
        customer.currentLocation = Constants.LOCATIONS_URI + "3000/";
        customer.avatar = avatar;
        customer.id = 4000;
        customer.currentLocation = "here";

        final ContentProvider mContentProvider = new CanaryAvatarContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_AVATAR;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_AVATAR, mContentProvider);


        final ContentProvider contentProvider = new CanaryCustomerContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_CUSTOMER;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_CUSTOMER, contentProvider);
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testContentValues() throws Exception {
        ContentValues content = CustomerDatabaseService.createContentValuesFromCustomer(customer);
        assertEquals(content.get("email"), "fake@gmail.com");
        assertEquals(content.get("username"), "naren");
    }

    public void testInsert() {
        AvatarDatabaseService.insertAvatar(avatar);
        CustomerDatabaseService.insertOrUpdateCustomer(customer);
    }
    
    public void testQuery() {
        Customer customer1 = CustomerDatabaseService.getCustomerFromId(customer.id);
        assertNotNull(customer1);
        assertEquals(customer1.email,customer.email);
    }

    public void testUpdate() {
        Customer customer1 = CustomerDatabaseService.getCustomerFromId(customer.id);
        assert customer1 != null;
        customer1.firstName = "New";
        customer1.lastName = "Name";
        CustomerDatabaseService.insertOrUpdateCustomer(customer1);
        Customer customer2 = CustomerDatabaseService.getCustomerFromId(customer.id);
        assertNotNull(customer2);
        assertEquals(customer2.getFullName(), "New Name");
    }

    public void testDelete() {
        CustomerDatabaseService.deleteCustomer(customer.id);
        Customer customer2 = CustomerDatabaseService.getCustomerFromId(customer.id);
        assertNull(customer2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(CanaryAvatarContentProvider.class);
    }
}
