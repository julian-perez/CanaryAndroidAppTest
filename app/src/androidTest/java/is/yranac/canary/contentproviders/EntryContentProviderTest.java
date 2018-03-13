package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.messages.LocationAndEntry;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class EntryContentProviderTest extends ProviderTestCase2<CanaryEntryContentProvider> {

    @Mock
    public Entry entry;

    @Mock
    Location location;

    @Mock
    VideoExport videoExport;

    /**
     * Constructor.
     */
    public EntryContentProviderTest() {
        super(CanaryEntryContentProvider.class, Constants.AUTHORITY_ENTRY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        entry = mock(Entry.class);
        entry.lastModified = new Date();
        entry.startTime = new Date();
        entry.endTime = new Date();
        entry.id = 7000;
        entry.locationUri = Constants.LOCATIONS_URI + "7000/";
        entry.description = "detection";
        entry.resourceUri = Constants.ENTRIES_URI + "7000/";
        entry.lastModified = new Date();
        entry.duration = 10;
        entry.entryType = "motion";

        location = mock(Location.class);
        location.id = 7000;
        location.name = "office";
        location.resourceUri = Constants.LOCATIONS_URI + "7000/";
        location.created = new Date();

        final ContentProvider contentProvider = new CanaryEntryContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_ENTRY;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY, contentProvider);

        final ContentProvider xContentProvider = new CanaryEntryCustomerContentProvider();
        ProviderInfo xProviderInfo = new ProviderInfo();
        xProviderInfo.authority = Constants.AUTHORITY_ENTRY_CUSTOMERS;
        xContentProvider.attachInfo(getMockContext(), xProviderInfo);
        xContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY_CUSTOMERS, xContentProvider);

        final ContentProvider mContentProvider = new CanaryLocationContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);

        final ContentProvider pContentProvider = new CanaryLabelContentProvider();
        ProviderInfo pProviderInfo = new ProviderInfo();
        pProviderInfo.authority = Constants.AUTHORITY_LABEL;
        pContentProvider.attachInfo(getMockContext(), pProviderInfo);
        pContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_LABEL, pContentProvider);

        final ContentProvider kContentProvider = new CanaryVideoExportContentProvider();
        ProviderInfo kProviderInfo = new ProviderInfo();
        kProviderInfo.authority = Constants.AUTHORITY_VIDEO_EXPORT;
        kContentProvider.attachInfo(getMockContext(), kProviderInfo);
        kContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_VIDEO_EXPORT, kContentProvider);
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testInsert() {
        List<Entry> entries = new ArrayList<>();
        entries.add(entry);
        LocationDatabaseService.insertLocation(location);
        Location mLoc = LocationDatabaseService.getLocationFromId(7000);
        assertNotNull(mLoc);
        assertEquals(location.id, mLoc.id);
        EntryDatabaseService.insertOrUpdateEntries(entries, Entry.DAILY_TIMELINE);
    }

    public void testGetLatestEntry() throws Exception {
        List<LocationAndEntry> entryLocationList = EntryDatabaseService.getLatestEntry();
        if (!entryLocationList.isEmpty()) {
            LocationAndEntry entryLocation = entryLocationList.get(0);
            Location loc = entryLocation.location;
            Entry ent = entryLocation.entry;
            assertEquals(loc.name, location.name);
            assertEquals(ent.description, entry.description);
        }else {
            assertTrue(true);
        }
    }

    public void testQuery() {
        Entry entry2 = EntryDatabaseService.getEntryFromEntryId(entry.id);
        assertNotNull(entry2);
        assertEquals(entry2.id, entry.id);
    }

    public void testUpdate() throws Exception {
        Entry beforeUpdate = EntryDatabaseService.getEntryFromEntryId(entry.id);
        entry.duration = 100;
        EntryDatabaseService.insertOrUpdateEntry(entry, Entry.DAILY_TIMELINE);
        Entry afterUpdate = EntryDatabaseService.getEntryFromEntryId(entry.id);
        assertNotNull(afterUpdate);
        assertNotNull(beforeUpdate);
        assertNotSame(beforeUpdate.duration,afterUpdate.duration);
    }

    public void testContentValues() throws Exception {
        ContentValues cV = EntryDatabaseService.createContentValuesFromEntry(entry, Entry.DAILY_TIMELINE);
        assertNotNull(cV);
        assertEquals(cV.get("duration").toString(), "10");
    }

    public void testDelete() {
        EntryDatabaseService.deleteEntry(entry.id);
        Entry entry1 = EntryDatabaseService.getEntryFromEntryId(entry.id);
        assertNull(entry1);
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(EntryContentProviderTest.class);
    }
}