package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.yranac.canary.contentproviders.CanaryFeatureFlagsContentProvider;
import is.yranac.canary.model.featureflag.CurrentLocationFeatureFlags;
import is.yranac.canary.model.featureflag.FeatureFlag;
import is.yranac.canary.util.Log;

/**
 * Created by Schroeder on 3/1/16.
 */
public class FeatureFlagDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "FeatureFlagDatabaseService";

    public static void insertFeatureFlags(List<FeatureFlag> featureFlags, int locationId) {

        List<FeatureFlag> currentFeatureFlags = getFeatureFlagsForLocation(locationId);

        for (FeatureFlag currentFeatureFlag : currentFeatureFlags) {

            if (!featureFlags.contains(currentFeatureFlag)) {
                deleteFeatureFlagForLocation(currentFeatureFlag, locationId);
            }
        }

        for (FeatureFlag featureFlag : featureFlags) {
            ContentValues contentValues = contentValuesFromFeatureFlag(featureFlag, locationId);

            String where = CanaryFeatureFlagsContentProvider.COLUMN_LOCATION_ID + " == ?";
            where += " AND ";
            where += CanaryFeatureFlagsContentProvider.COLUMN_NAME + " == ?";
            String[] whereArgs = {String.valueOf(locationId), featureFlag.key};
            int updated = contentResolver.update(CanaryFeatureFlagsContentProvider.CONTENT_URI,
                    contentValues, where, whereArgs);

            Log.i(LOG_TAG, "Updated " + updated);
            if (updated == 0) {
                contentResolver.insert(CanaryFeatureFlagsContentProvider.CONTENT_URI, contentValues);
            }
        }

        CurrentLocationFeatureFlags.getInstance().updateFeatureConfig(null);
    }

    public static void deleteFeatureFlagForLocation(FeatureFlag featureFlag, int locationId) {
        String where = CanaryFeatureFlagsContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += " AND ";

        where += CanaryFeatureFlagsContentProvider.COLUMN_NAME + " == ?";
        String[] whereArgs = {String.valueOf(locationId), featureFlag.key};
        contentResolver.delete(CanaryFeatureFlagsContentProvider.CONTENT_URI, where, whereArgs);
    }


    public static List<FeatureFlag> getFeatureFlagsForLocation(int locationId) {

        List<FeatureFlag> featureFlags = new ArrayList<>();


        String where = CanaryFeatureFlagsContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};

        Cursor cursor = contentResolver.query(CanaryFeatureFlagsContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return featureFlags;


        if (cursor.moveToFirst()) {
            do {
                featureFlags.add(featureFlagFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return featureFlags;

    }

    public static FeatureFlag featureFlagFromCursor(Cursor cursor) {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.enabled = cursor.getInt(cursor.getColumnIndex(CanaryFeatureFlagsContentProvider.COLUMN_ENABLED)) > 0;
        featureFlag.key = cursor.getString(cursor.getColumnIndex(CanaryFeatureFlagsContentProvider.COLUMN_NAME));
        String meta = cursor.getString(cursor.getColumnIndex(CanaryFeatureFlagsContentProvider.COLUMN_META_DATA));


        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>() {
        }.getType();

        featureFlag.meta = gson.fromJson(meta, stringStringMap);
        return featureFlag;
    }

    public static ContentValues contentValuesFromFeatureFlag(FeatureFlag featureFlag, int locationId) {
        String meta = featureFlag.meta == null ? "" : featureFlag.meta.toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryFeatureFlagsContentProvider.COLUMN_ENABLED, featureFlag.enabled);
        contentValues.put(CanaryFeatureFlagsContentProvider.COLUMN_META_DATA, meta);
        contentValues.put(CanaryFeatureFlagsContentProvider.COLUMN_NAME, featureFlag.key);
        contentValues.put(CanaryFeatureFlagsContentProvider.COLUMN_LOCATION_ID, locationId);

        return contentValues;
    }
}
