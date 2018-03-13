package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Assert;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.InvitationDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.Utils;

import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 6/1/16.
 */
public class InvitationContentProviderTest extends ProviderTestCase2<CanaryInvitationContentProvider> {

    @Mock
    Invitation invitation;

    @Mock
    Location location;

    @Mock
    Cursor cursor;

    /**
     * Constructor.
     */
    public InvitationContentProviderTest() {
        super(CanaryInvitationContentProvider.class, Constants.AUTHORITY_INVITATION);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        location = mock(Location.class);
        location.id = 1500;
        location.resourceUri = Constants.LOCATIONS_URI + "1500/";
        location.created = new Date();

        cursor = mock(Cursor.class);

        invitation = mock(Invitation.class);
        invitation.id = 1500;
        invitation.email = "fake@gmail.com";
        invitation.firstName = "fake";
        invitation.lastName = "fake";
        invitation.inviter = "mike";
        invitation.phone = "813413914";
        invitation.resourceUri = Constants.INVITATION_URI + "1500/";
        invitation.locationUri = location.resourceUri;
        invitation.status = "sent";
        invitation.userType = "new";

        final ContentProvider mContentProvider = new CanaryLocationContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        MockContentResolver mMockContentResolver = getMockContentResolver();
        mMockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);

        final ContentProvider contentProvider = new CanaryInvitationContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_INVITATION;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        mMockContentResolver.addProvider(Constants.AUTHORITY_INVITATION, contentProvider);

        BaseDatabaseService.contentResolver = mMockContentResolver;
    }

    public void testInsert() throws Exception {
        LocationDatabaseService.insertLocation(location);
        Location mLocation = LocationDatabaseService.getLocationFromId(1500);
        assertNotNull(mLocation);
        assertEquals(location.id, mLocation.id);
        InvitationDatabaseService.insertInvitation(invitation);
    }

    public void testCreateContentValuesFromInvitation() throws Exception {
        ContentValues cV = InvitationDatabaseService.createContentValuesFromInvitation(invitation);
        assertNotNull(cV);
        assertEquals(cV.get("inviter"), invitation.inviter);
    }

    public void testQuery() throws Exception {
        Invitation mInvitation = InvitationDatabaseService.getInvitationFromCursor(cursor);
        assertNotNull(mInvitation);
        int mLocation = Utils.getIntFromResourceUri(invitation.locationUri);
        List<Invitation> xInvitation = InvitationDatabaseService.getInvitationsAtLocation(mLocation);
        Invitation newInvitation = xInvitation.get(0);
        assertEquals(newInvitation.id, invitation.id);
    }

    public void testUpdate() throws Exception {
        InvitationDatabaseService.insertInvitation(invitation.id, invitation);
        int mLocation = Utils.getIntFromResourceUri(invitation.locationUri);
        List<Invitation> mInvitation = InvitationDatabaseService.getInvitationsAtLocation(mLocation);
        Invitation beforeUpdate = mInvitation.get(0);
        invitation.firstName = "newFake";
        InvitationDatabaseService.insertInvitation(invitation.id, invitation);
        List<Invitation> xInvitation = InvitationDatabaseService.getInvitationsAtLocation(mLocation);
        Invitation afterUpdate = xInvitation.get(0);
        assertEquals(beforeUpdate.id, afterUpdate.id);
        assertNotSame(beforeUpdate.firstName, afterUpdate.firstName);
    }

    public void testDelete() throws Exception {
        InvitationDatabaseService.deleteInvitation(invitation.id);
        List<Invitation> mInvitation = InvitationDatabaseService.getInvitationsAtLocation(location.id);
        Assert.assertSame(mInvitation.size(), 0);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(InvitationContentProviderTest.class);
    }
}
