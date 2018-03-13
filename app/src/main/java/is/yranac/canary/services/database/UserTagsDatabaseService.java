package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanaryUserTagsContentProvider;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 1/12/16.
 */
public class UserTagsDatabaseService extends BaseDatabaseService {
    public static void deleteTag(String tag) {
        String where = CanaryUserTagsContentProvider.COLUMN_NAME + " == ? COLLATE NOCASE";
        String[] whereArgs = {tag};
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryUserTagsContentProvider.COLUMN_DELETED, true);

        contentResolver.update(CanaryUserTagsContentProvider.CONTENT_URI, contentValues, where, whereArgs);
    }

    public static void insertTag(String tag, boolean override) {
        List<String> defaults = getDefaultTags();
        for (String defaultTag : defaults) {
            if (defaultTag.equalsIgnoreCase(tag))
                return;
        }

        if (override) {
            deleteTag(tag);
        }
        String where = CanaryUserTagsContentProvider.COLUMN_NAME + " == ? COLLATE NOCASE";
        String[] whereArgs = {tag};

        ContentValues contentValues = new ContentValues();
        if (override)
            contentValues.put(CanaryUserTagsContentProvider.COLUMN_DELETED, false);

        contentValues.put(CanaryUserTagsContentProvider.COLUMN_NAME, tag);

        int updated = contentResolver.update(CanaryUserTagsContentProvider.CONTENT_URI, contentValues, where, whereArgs);

        if (updated == 0) {
            contentValues.put(CanaryUserTagsContentProvider.COLUMN_DELETED, false);
            contentResolver.insert(CanaryUserTagsContentProvider.CONTENT_URI, contentValues);
        }
    }


    public static List<String> getDefaultTags() {
        List<String> tags = new ArrayList<>();

        // Add default tags
        List<String> defaultTagTitles;
        if (Utils.isNotProd()) {
            defaultTagTitles = Arrays.asList(CanaryApplication.getContext().getResources().getStringArray(R.array.testing_cv_tags));

        } else {
            defaultTagTitles = Arrays.asList(CanaryApplication.getContext().getResources().getStringArray(R.array.default_cv_tags));
        }

        for (String defaultTagTitle : defaultTagTitles) {
            tags.add(defaultTagTitle);
        }

        for (String extraTag : Constants.extraTags) {
            tags.add(extraTag);
        }

        // Add customers for location
        for (Customer customer : CustomerDatabaseService.getCustomersAtLocation(UserUtils.getLastViewedLocationId())) {
            tags.add(customer.firstName);
        }
        return tags;
    }

    public static List<String> getUserTags() {

        List<String> tags = new ArrayList<>();

        String where = CanaryUserTagsContentProvider.COLUMN_DELETED + " != ?";
        String[] whereArgs = {"1"};
        Cursor cursor = contentResolver.query(CanaryUserTagsContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return tags;

        if (cursor.moveToFirst()) {
            do {
                tags.add(cursor.getString(cursor.getColumnIndex(CanaryUserTagsContentProvider.COLUMN_NAME)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tags;
    }


    public static void deleteTags() {
        contentResolver.delete(CanaryUserTagsContentProvider.CONTENT_URI, null, null);
    }
}
