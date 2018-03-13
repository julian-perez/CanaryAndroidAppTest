package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.Date;
import java.util.Map;

import is.yranac.canary.Constants;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.EmergencyContactDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.Utils;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class EmergencyContentProviderTest extends ProviderTestCase2<CanaryEmergencyContactsContentProvider> {

    @Mock
    public EmergencyContact emergencyContact;

    @Mock
    Location location;

    /**
     * Constructor.
     */
    public EmergencyContentProviderTest() {
        super(CanaryEmergencyContactsContentProvider.class, Constants.AUTHORITY_EMERGENCY_CONTACTS);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        location = mock(Location.class);
        location.id = 6000;
        location.resourceUri = Constants.LOCATIONS_URI + "6000/";
        location.created = new Date();

        emergencyContact = mock(EmergencyContact.class);
        emergencyContact.id = 6000;
        emergencyContact.locationUri = location.resourceUri;
        emergencyContact.contactType = EmergencyContact.ContactType.ems.toString();

        final ContentProvider contentProvider = new CanaryLocationContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_LOCATION;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, contentProvider);

        final ContentProvider mContentProvider = new CanaryEmergencyContactsContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_EMERGENCY_CONTACTS;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_EMERGENCY_CONTACTS, mContentProvider);
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testInsert() {
        LocationDatabaseService.insertLocation(location);
        Location mLocation = LocationDatabaseService.getLocationFromId(6000);
        assertNotNull(mLocation);
        assertEquals(mLocation.id, location.id);

        EmergencyContactDatabaseService.insertEmergencyContact(emergencyContact);
    }

    public void testQuery() {
        int locationId = Utils.getIntFromResourceUri(emergencyContact.locationUri);
        Map<EmergencyContact.ContactType, EmergencyContact> map = EmergencyContactDatabaseService.getEmergencyContacts(locationId);
        assertEquals(map.size(), 1);
        EmergencyContact mEmergencyContact = map.get(EmergencyContact.ContactType.ems);
        assertEquals(emergencyContact.id,mEmergencyContact.id);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(EmergencyContentProviderTest.class);

    }
}
