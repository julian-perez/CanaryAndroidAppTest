package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;

import is.yranac.canary.CanaryApplication;

/**
 * Created by Schroeder on 9/17/14.
 */
public class BaseDatabaseService {

    public static ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();


    private static final String TAG = "BaseDatabaseService";

    /**
     * Function to only insert a value into the content value if it is not null
     *
     * @param contentValues the values object that they are to be inserted into
     * @param column        Column to update
     * @param value         Value of the Column
     */
    protected static void setIfNotNull(ContentValues contentValues, String column, String value) {

        if (value != null) {
            contentValues.put(column, value);
        }
    }

    /**
     * Function to only insert a value into the content value if it is not zero
     *
     * @param contentValues the values object that they are to be inserted into
     * @param column        Column to update
     * @param value         Value of the Column
     */
    protected static void setIfNotZero(ContentValues contentValues, String column, int value) {

        if (value != 0) {
            contentValues.put(column, value);
        }
    }


    public static void purgeDatabase() {
        LocationDatabaseService.deleteLocations();
        CustomerDatabaseService.deleteCustomers();
        DeviceTokenDatabaseService.deleteTokens();
        UserTagsDatabaseService.deleteTags();
    }




//    public static void setContentResolve(MockContentResolver mockContentResolver) {
//        contentResolver = mockContentResolver;
//    }

}
