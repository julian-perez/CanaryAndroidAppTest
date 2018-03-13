package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryModeContentProvider;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 7/30/14.
 */
public class ModeDatabaseService {

    private static final String LOG_TAG = "ModeUpdateHandler";

    public static void insertModes(List<Mode> modes) {
        for (Mode mode : modes) {
            insertMode(mode);
        }
    }

    public static void insertMode(Mode mode) {

        ContentValues values = createContentValuesFromMode(mode);

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        contentResolver.insert(CanaryModeContentProvider.CONTENT_URI, values);

    }

    public static ContentValues createContentValuesFromMode(Mode mode) {

        int id = mode.id;
        String name = mode.name.toLowerCase();

        ContentValues values = new ContentValues();

        values.put(CanaryModeContentProvider.COLUMN_ID, id);
        values.put(CanaryModeContentProvider.COLUMN_NAME, name);
        return values;
    }

    // ******************************************
    // *** Get a record
    // ******************************************
    public static Mode getModeFromId(int id) {
        Mode mode = Mode.Error();

        String where = CanaryModeContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(id)};

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(CanaryModeContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return mode;

        if (cursor.moveToFirst()) {
            mode = getModeFromCursor(cursor);
        }
        cursor.close();

        return mode;
    }

    public static Mode getModeFromResourceUri(String resourceUri) {
        Mode mode = Mode.Error();

        String where = CanaryModeContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(Utils.getIntFromResourceUri(resourceUri))};

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(CanaryModeContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return mode;

        if (cursor.moveToFirst()) {
            mode = getModeFromCursor(cursor);
        }
        cursor.close();

        return mode;
    }

    // *****************************************************
    // *** Get Device from Cursor
    // *****************************************************
    public static Mode getModeFromCursor(Cursor cursor) {
        Mode mode = new Mode();

        mode.id = cursor.getInt(cursor.getColumnIndex(CanaryModeContentProvider.COLUMN_ID));
        mode.name = cursor.getString(cursor.getColumnIndex(CanaryModeContentProvider.COLUMN_NAME));
        mode.resourceUri = Utils.buildResourceUri(Constants.MODES_URI, mode.id);

        return mode;
    }

    public static Mode currentLocationModeCheck() {
        Location currentLocation = UserUtils.getLastViewedLocation();
        if (currentLocation == null) {
            return Mode.Error();
        }

        if (currentLocation.currentMode == null) {
            return Mode.Error();
        }

        return currentLocation.currentMode;

    }

    public static Mode getModeForLocation(int locationId) {

        Location currentLocation = LocationDatabaseService.getLocationFromId(locationId);
        if (currentLocation == null) {
            return Mode.Error();
        }

        return currentLocation.currentMode;
    }

    public static List<Mode> getAllModes() {
        List<Mode> modes = new ArrayList<>();
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(CanaryModeContentProvider.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                modes.add(getModeFromCursor(cursor));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return modes;
    }
}
