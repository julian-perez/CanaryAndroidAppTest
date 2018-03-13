package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.UserTagsDatabaseService;

/**
 * Created by narendramanoharan on 5/30/16.
 */
public class UserTagsContentDatabaseTest extends ProviderTestCase2<CanaryUserTagsContentProvider> {

    @Mock
    String tag = "name";

    @Mock
    Cursor cursor;

    /**
     * Constructor.
     */
    public UserTagsContentDatabaseTest() {
        super(CanaryUserTagsContentProvider.class, Constants.AUTHORITY_USER_TAGS);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        final ContentProvider contentProvider = new CanaryUserTagsContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_USER_TAGS;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_USER_TAGS, contentProvider);
        BaseDatabaseService.contentResolver = getMockContentResolver();
    }

    public void testInsert() throws Exception {
        UserTagsDatabaseService.insertTag(tag,false);
    }

    public void testQuery() throws Exception {
        List<String> mTags = UserTagsDatabaseService.getUserTags();
        assertNotNull(mTags);
        assertEquals(mTags.get(0),tag);
        List<String> tags = UserTagsDatabaseService.getDefaultTags();
        assertEquals(tags.get(0),"People");
    }

    public void testDelete() throws Exception {
        UserTagsDatabaseService.deleteTags();
        List<String> tags = UserTagsDatabaseService.getUserTags();
        assertEquals(tags.size(),0);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(UserTagsContentDatabaseTest.class);
    }
}
