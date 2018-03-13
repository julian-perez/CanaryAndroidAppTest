package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.EntryCustomersDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 6/1/16.
 */
public class EntryCustomersContentProviderTest extends ProviderTestCase2<CanaryEntryCustomerContentProvider> {

    @Mock
    Customer customer;

    @Mock
    Entry entry;

    @Mock
    Location location;

    /**
     * Constructor.
     */
    public EntryCustomersContentProviderTest() {
        super(CanaryEntryCustomerContentProvider.class, Constants.AUTHORITY_ENTRY_CUSTOMERS);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        customer = mock(Customer.class);

        customer.email = "fake@gmail.com";
        customer.firstName = "First";
        customer.lastName = "Last";
        customer.phone = "1234567890";
        customer.username = "naren";
        customer.languagePreference = "eng";
        customer.resourceUri = Constants.CUSTOMER_URI + "8000/";
        customer.currentLocation = Constants.LOCATIONS_URI + "8000/";
        customer.id = 8000;
        customer.currentLocation = "here";

        location = mock(Location.class);
        location.id = 8000;
        location.created = new Date();
        location.resourceUri = Constants.LOCATIONS_URI + "8000/";

        entry = mock(Entry.class);
        entry.lastModified = new Date();
        entry.startTime = new Date();
        entry.endTime = new Date();
        entry.id = 7000;
        entry.locationUri = Constants.LOCATIONS_URI + "8000/";
        entry.description = "detection";
        entry.resourceUri = Constants.ENTRIES_URI + "8000/";
        entry.lastModified = new Date();
        entry.duration = 10;
        entry.entryType = "motion";


        final ContentProvider contentProvider = new CanaryEntryContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_ENTRY;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY, contentProvider);

        final ContentProvider mContentProvider = new CanaryLocationContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);
        BaseDatabaseService.contentResolver = mockContentResolver;
    }


    @Test
    public void testInsert() throws Exception {
        LocationDatabaseService.insertLocation(location);
        Location loc = LocationDatabaseService.getLocationFromId(location.id);
        assertNotNull(loc);
        assertEquals(loc.id, location.id);

        EntryDatabaseService.insertOrUpdateEntry(entry, 1);
        Entry mEntry = EntryDatabaseService.getEntryFromEntryId(entry.id);
        assertNotNull(mEntry);
        assertEquals(entry.id, mEntry.id);

        List<String> customerList = new ArrayList<>();
        customerList.add(customer.resourceUri);
        EntryCustomersDatabaseService.insertCustomersForEntry(customerList, entry.id);

    }

    @Test
    public void testQuery() throws Exception {
        List<String> entryCustomer =  EntryCustomersDatabaseService.getCustomersForEntry(entry.id);
        String mCustomer = entryCustomer.get(0);
        assertEquals(customer.resourceUri, mCustomer);
    }

    @Test
    public void testDelete() throws Exception {
        int entryId = 10230;
        EntryCustomersDatabaseService.deleteCustomersForEntry(entryId);
        List<String> entryCustomer =  EntryCustomersDatabaseService.getCustomersForEntry(entryId);
        assertEquals(entryCustomer.size(), 0);
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(EntryCustomersContentProviderTest.class);
    }
}
