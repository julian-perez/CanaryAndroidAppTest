package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.entry.DisplayMeta;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.Notified;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.NotifiedDatabaseService;
import is.yranac.canary.util.DateUtil;


/**
 * Created by narendramanoharan on 6/1/16.
 */
public class NotifiedContentProviderTest extends ProviderTestCase2<CanaryNotifiedContentProvider> {

    Notified notified;

    Location location;

    Entry entry;

    DisplayMeta displayMeta;

    /**
     * Constructor.
     */
    public NotifiedContentProviderTest() {
        super(CanaryNotifiedContentProvider.class, Constants.AUTHORITY_NOTIFIED);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        notified = new Notified();
        notified.eventCreated = DateUtil.convertApiStringToDate("2016-11-15T20:58:41");
        notified.eventId = 0;
        notified.detectionThreshold = 0.20;
        notified.nonBackgroundScore = 0.6213958799;

        displayMeta = new DisplayMeta();
        displayMeta.notified = notified;


        entry = new Entry();
        entry.lastModified = new Date();
        entry.startTime = new Date();
        entry.endTime = new Date();

        entry.id = 3500;
        entry.locationUri = Constants.LOCATIONS_URI + "3500/";
        entry.description = "detection";
        entry.resourceUri = Constants.ENTRIES_URI + "3500/";
        entry.lastModified = new Date();
        entry.duration = 10;
        entry.entryType = "motion";
        entry.displayMeta = displayMeta;

        location = new Location();
        location.id = 3500;
        location.name = "office";
        location.resourceUri = Constants.LOCATIONS_URI + "3500/";
        location.created = new Date();

        List<Entry> entries = new ArrayList<>();
        entries.add(entry);

        MockContentResolver mockContentResolver = getMockContentResolver();


        final ContentProvider contentProvider = new CanaryEntryContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_ENTRY;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY, contentProvider);

        final ContentProvider mContentProvider = new CanaryLocationContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);


        final ContentProvider xContentProvider = new CanaryEntryCustomerContentProvider();
        ProviderInfo xProviderInfo = new ProviderInfo();
        xProviderInfo.authority = Constants.AUTHORITY_ENTRY_CUSTOMERS;
        xContentProvider.attachInfo(getMockContext(), xProviderInfo);
        xContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY_CUSTOMERS, xContentProvider);


        BaseDatabaseService.contentResolver = mockContentResolver;
        LocationDatabaseService.insertLocation(location);
        EntryDatabaseService.insertOrUpdateEntries(entries, Entry.DAILY_TIMELINE);
    }

    @Test
    public void testInsert() throws Exception {
        Entry mEntry = EntryDatabaseService.getEntryFromEntryId(entry.id);
        assertNotNull(mEntry);
        assertEquals(entry.id, mEntry.id);

        assertTrue(
                mEntry.displayMeta != null
                        && mEntry.displayMeta.notified != null
                        && mEntry.displayMeta.notified.eventId == notified.eventId);
    }

    @Test
    public void testQuery() throws Exception {
        Notified notified = NotifiedDatabaseService.getNofificationStatus(entry.id);
        assertTrue(notified != null && this.notified.eventId == notified.eventId);
    }

    @Test
    public void testDelete() throws Exception {

        Notified notified = NotifiedDatabaseService.getNofificationStatus(entry.id);
        assertTrue(notified != null);

        EntryDatabaseService.deleteEntry(entry.id);
        Notified notifiedCheck = NotifiedDatabaseService.getNofificationStatus(entry.id);
        assertTrue(notifiedCheck == null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        EntryDatabaseService.deleteEntry(entry.id);
        
        scrubClass(NotifiedContentProviderTest.class);
    }

}
