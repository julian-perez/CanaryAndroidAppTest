package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.contentproviders.CanaryEntryCustomerContentProvider;

/**
 * Created by Schroeder on 3/30/15.
 */
public class EntryCustomersDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "EntryCustomersDatabaseService";

    public static void insertCustomersForEntry(List<String> customers, long entryId) {

        if (customers == null)
            return;

        for (String customerUri : customers) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CanaryEntryCustomerContentProvider.COLUMN_ENTRY_ID, entryId);
            contentValues.put(CanaryEntryCustomerContentProvider.COLUMN_CUSTOMER_RESOURCE_URI, customerUri);
            contentResolver.insert(CanaryEntryCustomerContentProvider.CONTENT_URI, contentValues);
        }

    }

    public static void deleteCustomersForEntry(long entryId) {
        String where = CanaryEntryCustomerContentProvider.COLUMN_ENTRY_ID + " == ?";
        String whereArgs[] = {String.valueOf(entryId)};
        contentResolver.delete(CanaryEntryCustomerContentProvider.CONTENT_URI, where, whereArgs);

    }

    public static List<String> getCustomersForEntry(long entryId) {

        String where = CanaryEntryCustomerContentProvider.COLUMN_ENTRY_ID + " == ?";
        String whereArgs[] = {String.valueOf(entryId)};

        Cursor cursor = contentResolver.query(CanaryEntryCustomerContentProvider.CONTENT_URI, null, where, whereArgs, null);

        List<String> customerUris = new ArrayList<>();

        if (cursor == null)
            return customerUris;

        if (cursor.moveToFirst()) {
            do {
                customerUris.add(cursor.getString(cursor.getColumnIndex(CanaryEntryCustomerContentProvider.COLUMN_CUSTOMER_RESOURCE_URI)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return customerUris;
    }
}
