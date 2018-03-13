package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Assert;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.ModeDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by narendramanoharan on 6/1/16.
 */
public class ModeContentProviderTest extends ProviderTestCase2<CanaryModeContentProvider> {

    @Mock
    Mode mode;

    @Mock
    Cursor cursor;


    /**
     * Constructor.
    */
    public ModeContentProviderTest() {
        super(CanaryModeContentProvider.class, Constants.AUTHORITY_MODE);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mode = mock(Mode.class);
        mode.name = "privacy";
        mode.id = 3;
        mode.resourceUri = Constants.MODES_URI + "3/";

        cursor = mock(Cursor.class);

        final ContentProvider mContentProvider = new CanaryModeContentProvider();
        ProviderInfo mProviderInfo = new ProviderInfo();
        mProviderInfo.authority = Constants.AUTHORITY_MODE;
        mContentProvider.attachInfo(getMockContext(), mProviderInfo);
        mContentProvider.onCreate();
        MockContentResolver mockContentResolver = getMockContentResolver();
        mockContentResolver.addProvider(Constants.AUTHORITY_MODE, mContentProvider);


        final ContentProvider contentProvider = new CanaryFeatureFlagsContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = Constants.AUTHORITY_FEATURE_FLAG;
        contentProvider.attachInfo(getMockContext(), providerInfo);
        contentProvider.onCreate();
        mockContentResolver.addProvider(Constants.AUTHORITY_FEATURE_FLAG, contentProvider);

        BaseDatabaseService.contentResolver = mockContentResolver;

    }

    public void testInsert() throws Exception {
        ArrayList<Mode> modes = new ArrayList<>();
        modes.add(mode);
        ModeDatabaseService.insertModes(modes);
    }

    public void testQuery() throws Exception {
        Mode xMode = ModeDatabaseService.getModeFromCursor(cursor);
        Assert.assertNotNull(xMode);
        Mode mMode = ModeDatabaseService.getModeFromId(mode.id);
        assertEquals(mMode.id, mode.id);
        Mode mode1 = ModeDatabaseService.getModeFromResourceUri(mode.resourceUri);
        assertEquals(mode1.name, mMode.name);
        List<Mode> modeList = ModeDatabaseService.getAllModes();
        int size = modeList.size();
        for (int i = 0; i < size ; i++) {
            if(modeList.get(i).id == mode.id) {
                assertEquals(mode.name, modeList.get(i).name);
            }
        }
    }


    public void testUpdate() throws Exception {
        ModeDatabaseService.insertMode(mode);
        Mode mMode = ModeDatabaseService.getModeFromId(mode.id);
        assertNotNull(mMode);
        mode.name = "night";
        ModeDatabaseService.insertMode(mode);
        Mode xMode = ModeDatabaseService.getModeFromId(mode.id);
        assertNotNull(xMode);
        assertEquals(mMode.id, xMode.id);
        assertNotSame(mMode.name, xMode.name);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scrubClass(ModeContentProviderTest.class);

    }

}

