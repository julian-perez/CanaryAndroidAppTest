package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.featureflag.FeatureFlag;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.FeatureFlagDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;

import static org.mockito.Mockito.mock;

/**
 * Created by Schroeder on 3/2/16.
 */
public class FeatureFlagContentProviderTest extends ProviderTestCase2<CanaryFeatureFlagsContentProvider> {

        @Mock
        public FeatureFlag testFeatureFlag;

        @Mock
        Location location;

        @Mock
        Cursor cursor;

        /**
         * Constructor.
         */
        public FeatureFlagContentProviderTest() {
                super(CanaryFeatureFlagsContentProvider.class, Constants.AUTHORITY_FEATURE_FLAG);
        }

        @Override
        public void setUp() throws Exception {
                super.setUp();

                testFeatureFlag = mock(FeatureFlag.class);
                testFeatureFlag.enabled = true;
                testFeatureFlag.key = "test_flag";
                testFeatureFlag.meta = new HashMap<>();
                testFeatureFlag.meta.put("test_flag", "1");

                location = mock(Location.class);
                location.id = 9000;
                location.created = new Date();
                location.resourceUri = Constants.LOCATIONS_URI + "9000/";

                final ContentProvider mContentProvider = new CanaryLocationContentProvider();
                ProviderInfo mProviderInfo = new ProviderInfo();
                mProviderInfo.authority = Constants.AUTHORITY_LOCATION;
                mContentProvider.attachInfo(getMockContext(), mProviderInfo);
                mContentProvider.onCreate();
                MockContentResolver mMockContentResolver = getMockContentResolver();
                mMockContentResolver.addProvider(Constants.AUTHORITY_LOCATION, mContentProvider);

                final ContentProvider contentProvider = new CanaryFeatureFlagsContentProvider();
                ProviderInfo providerInfo = new ProviderInfo();
                providerInfo.authority = Constants.AUTHORITY_FEATURE_FLAG;
                contentProvider.attachInfo(getMockContext(), providerInfo);
                contentProvider.onCreate();
                mMockContentResolver.addProvider(Constants.AUTHORITY_FEATURE_FLAG, contentProvider);

                BaseDatabaseService.contentResolver = mMockContentResolver;
        }


        public void testInsert() {
                LocationDatabaseService.insertLocation(location);
                Location mLocation = LocationDatabaseService.getLocationFromId(9000);
                assertNotNull(mLocation);
                assertEquals(location.id, mLocation.id);

                ArrayList<FeatureFlag> list = new ArrayList<>();
                list.add(testFeatureFlag);
                FeatureFlagDatabaseService.insertFeatureFlags(list, location.id);
        }


        public void testQuery() {
                List<FeatureFlag> mFeatureFlags = FeatureFlagDatabaseService.getFeatureFlagsForLocation(location.id);
                FeatureFlag featureFlag = mFeatureFlags.get(0);
                assertEquals(featureFlag.meta, testFeatureFlag.meta);
        }


        public void testDelete() {
                FeatureFlagDatabaseService.deleteFeatureFlagForLocation(testFeatureFlag, location.id);
                List<FeatureFlag> featureFlags = FeatureFlagDatabaseService.getFeatureFlagsForLocation(location.id);
                assertEquals(featureFlags.size(), 0);
        }

        @Override
        protected void tearDown() throws Exception {
                super.tearDown();
                scrubClass(FeatureFlagContentProviderTest.class);
        }
}
