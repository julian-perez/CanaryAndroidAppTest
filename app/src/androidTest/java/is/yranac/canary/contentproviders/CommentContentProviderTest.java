package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.comment.Comment;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.CommentDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 1/9/15.
 */
public class CommentContentProviderTest extends ProviderTestCase2<CanaryCommentContentProvider> {

    @Mock
    public Comment comment;

    @Mock
    Cursor cursor;

    @Mock
    Entry entry;

    @Mock
    Location mLocation;

    /**
     * Constructor.
     */
    public CommentContentProviderTest() {
        super(CanaryCommentContentProvider.class, Constants.AUTHORITY_COMMENT);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        cursor = mock(Cursor.class);
        comment = mock(Comment.class);
        mLocation = mock(Location.class);

        mLocation.id = 3000;
        mLocation.created = new Date();
        mLocation.resourceUri = Constants.LOCATIONS_URI + "3000/";

        entry = mock(Entry.class);
        entry.lastModified = new Date();
        entry.startTime = new Date();
        entry.endTime = new Date();
        entry.lastModified = new Date();
        entry.duration = 10;
        entry.resourceUri = "/v1/entry/3000/";
        entry.id = 3000;
        entry.locationUri = "/v1/locations/3000";

        comment.entryUri = "/v1/entry/3000/";
        comment.id = 3000;
        comment.body = "hello";

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

        final ContentProvider xContentProvider = new CanaryCustomerContentProvider();
        ProviderInfo xProviderInfo = new ProviderInfo();
        xProviderInfo.authority = Constants.AUTHORITY_CUSTOMER;
        xContentProvider.attachInfo(getMockContext(), xProviderInfo);
        xContentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_CUSTOMER, xContentProvider);
        BaseDatabaseService.contentResolver = mockContentResolver;
    }

    public void testContentValues() throws Exception {
        ContentValues content = CommentDatabaseService.createContentValuesFromComment(comment);
        assertEquals(content.get("body"), "hello");
        assertEquals(content.get("id"), 3000);
    }

    public void testInsert() {
        LocationDatabaseService.insertLocation(mLocation);
        EntryDatabaseService.insertOrUpdateEntry(entry, 1);
        CommentDatabaseService.updateOrInsertComment(comment);
    }

    public void testQuery() {
        CommentDatabaseService.updateOrInsertComment(comment);
        List<Comment> comments = CommentDatabaseService.getCommentsForEntry(3000);
        assertNotNull(comments);
        assertEquals(comments.get(0).body, comment.body);
    }

    public void testGetFromCursor() throws Exception {
        Comment mComment = CommentDatabaseService.getCommentFromCursor(cursor);
        assertNotNull(mComment);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(CommentContentProviderTest.class);

    }
}
