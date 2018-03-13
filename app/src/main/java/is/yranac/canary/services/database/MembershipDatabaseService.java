package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryMembershipContentProvider;
import is.yranac.canary.model.membership.MembershipPresence;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.Utils;

/**
 * Created by michaelschroeder on 1/10/18.
 */

public class MembershipDatabaseService {

    private static final String LOG_TAG = "MembershipDatabaseService";

    public static void insertMemberships(Context context, int location, List<MembershipPresence> memberships) {
        for (MembershipPresence membership : memberships) {
            insertMembershipPresence(context, membership);
        }
    }

    public static void insertMembershipPresence(Context context, MembershipPresence presence) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CanaryMembershipContentProvider.CONTENT_URI, createContentValuesFromCustomer(presence));

    }

    public static MembershipPresence getMembershipsForLocation(Context context, int location, int customerId) {
        ContentResolver resolver = context.getContentResolver();

        String where = CanaryMembershipContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += " AND ";
        where += CanaryMembershipContentProvider.COLUMN_CUSTOMER_ID + " == ?";
        String whereArgs[] = {String.valueOf(location), String.valueOf(customerId)};

        Cursor cursor = resolver.query(CanaryMembershipContentProvider.CONTENT_URI, null, where, whereArgs, CanaryMembershipContentProvider.COLUMN_CUSTOMER_ID + " ASC");

        if (cursor == null)
            return null;


        Log.i(LOG_TAG, String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do {
                return getMembershipFromCursor(cursor);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return null;
    }

    /**
     * Create a Membership object from a cursor row
     *
     * @param cursor cursor from which a membership object can be created from
     * @return ContentValues object with the location information
     */
    public static MembershipPresence getMembershipFromCursor(Cursor cursor) {
        MembershipPresence membership = new MembershipPresence();

        int customerId = cursor.getInt(cursor.getColumnIndex(CanaryMembershipContentProvider.COLUMN_CUSTOMER_ID));
        membership.customerUri = Utils.buildResourceUri(Constants.CUSTOMER_URI, customerId);

        int locationId = cursor.getInt(cursor.getColumnIndex(CanaryMembershipContentProvider.COLUMN_LOCATION_ID));
        membership.locationUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);

        membership.sendPresenceNotifications = cursor.getInt(cursor.getColumnIndex(CanaryMembershipContentProvider.COLUMN_PRESENCE_NOTIFICATIONS)) > 0;

        return membership;
    }

    /**
     * Create a ContentValues object from a customer object
     *
     * @param membership Membership to convert
     * @return ContentValues object with the location information
     */
    public static ContentValues createContentValuesFromCustomer(MembershipPresence membership) {
        ContentValues values = new ContentValues();

        int customerId = Utils.getIntFromResourceUri(membership.customerUri);
        int locationId = Utils.getIntFromResourceUri(membership.locationUri);
        boolean presenceNotifications = membership.sendPresenceNotifications;
        Log.i(LOG_TAG, "Location = " + locationId + ", customer = " + customerId + ", presenceNotifications = " + presenceNotifications);

        values.put(CanaryMembershipContentProvider.COLUMN_CUSTOMER_ID, customerId);
        values.put(CanaryMembershipContentProvider.COLUMN_LOCATION_ID, locationId);
        values.put(CanaryMembershipContentProvider.COLUMN_PRESENCE_NOTIFICATIONS, presenceNotifications);
        return values;
    }
}
