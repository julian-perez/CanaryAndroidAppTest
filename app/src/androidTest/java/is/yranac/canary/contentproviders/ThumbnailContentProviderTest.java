package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 5/31/16.
 */
public class ThumbnailContentProviderTest extends ProviderTestCase2<CanaryThumbnailContentProvider> {


    @Mock
    Entry entry;

    @Mock
    Thumbnail thumbnail;


    @Mock
    Location location;

    /**
     * Constructor.
     *
     */
    public ThumbnailContentProviderTest() {
        super(CanaryThumbnailContentProvider.class, Constants.AUTHORITY_THUMBNAIL);
    }

    @Before
    @Override
    public void setUp() throws Exception {

        super.setUp();

        entry = mock(Entry.class);
        entry.lastModified = new Date();
        entry.startTime = new Date();
        entry.endTime = new Date();
        entry.id = 2590;
        entry.locationUri = Constants.LOCATIONS_URI + "2590/";
        entry.description = "detection";
        entry.resourceUri = Constants.ENTRIES_URI + "2590/";
        entry.lastModified = new Date();
        entry.duration = 10;
        entry.entryType = "motion";

        location = mock(Location.class);
        location.id = 2590;
        location.name = "office";
        location.resourceUri = Constants.LOCATIONS_URI + "2590/";
        location.created = new Date();

        thumbnail = mock(Thumbnail.class);
        thumbnail.imageUrl = "www.fake.com/hello";
        thumbnail.device = "Alpha";
        thumbnail.entry = entry.resourceUri;

        final ContentProvider mContentProvider = new CanaryThumbnailContentProvider();
        ProviderInfo zProviderInfo = new ProviderInfo();
        zProviderInfo.authority = Constants.AUTHORITY_THUMBNAIL;
        mContentProvider.attachInfo(getMockContext(), zProviderInfo);
        mContentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_THUMBNAIL, mContentProvider);

        final ContentProvider contentProvider = new CanaryEntryContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_ENTRY;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY, contentProvider);


        final ContentProvider xContentProvider = new CanaryEntryCustomerContentProvider();
        ProviderInfo xProviderInfo = new ProviderInfo();
        xProviderInfo.authority = Constants.AUTHORITY_ENTRY_CUSTOMERS;
        xContentProvider.attachInfo(getMockContext(), xProviderInfo);
        xContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_ENTRY_CUSTOMERS, xContentProvider);

        final ContentProvider zContentProvider = new CanaryLocationContentProvider();
        ProviderInfo pProviderInfo = new ProviderInfo();
        pProviderInfo.authority = Constants.AUTHORITY_LOCATION;
        zContentProvider.attachInfo(getMockContext(), pProviderInfo);
        zContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, zContentProvider);
        
        
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    @Test
    public void testInsert() throws Exception {

        List<Entry> entries = new ArrayList<>();
        entries.add(entry);

        LocationDatabaseService.insertLocation(location);

        EntryDatabaseService.insertOrUpdateEntries(entries, Entry.DAILY_TIMELINE);
        Entry mEntry = EntryDatabaseService.getEntryFromEntryId(entry.id);
        assertNotNull(mEntry);
        assertEquals(entry.id, mEntry.id);

        ThumbnailDatabaseService.insertThumbnail(thumbnail);
    }

    @Test
    public void testQuery() throws Exception {
        ThumbnailDatabaseService.insertThumbnail(thumbnail);
        List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
        assertNotNull(thumbnails);
    }

    @Test
    public void testUpdate() throws Exception {
        ThumbnailDatabaseService.insertThumbnail(thumbnail);
        List<Thumbnail> mThumbnail = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
        assertEquals("Alpha",thumbnail.device);
        Thumbnail beforeUpdate = mThumbnail.get(0);
        thumbnail.device = "Beta";
        ThumbnailDatabaseService.insertThumbnail(thumbnail);
        assertEquals("Beta",thumbnail.device);
        List<Thumbnail> xThumbnail = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
        Thumbnail afterUpdate = xThumbnail.get(0);
        assertEquals(beforeUpdate.imageUrl, afterUpdate.imageUrl);
        assertNotSame(beforeUpdate.device,afterUpdate.device);
    }

    @Test
    public void testDelete() throws Exception {
        ThumbnailDatabaseService.deleteThumbnail(entry.id);
        List<Thumbnail> mThumbnail = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
        assertTrue(mThumbnail.isEmpty());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(ThumbnailContentProviderTest.class);

    }
}
