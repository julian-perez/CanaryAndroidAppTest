package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryInvitationContentProvider;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.util.Utils;

public class InvitationDatabaseService {
    private static final String LOG_TAG = "InvitationDatabaseService";

    public static void insertInvitation(int id, Invitation invitation) {
        invitation.id = id;
        insertInvitation(invitation);
    }

    public static void insertInvitation(Invitation invitation) {
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        
        contentResolver.insert(CanaryInvitationContentProvider.CONTENT_URI, createContentValuesFromInvitation(invitation));
    }

    public static void updateInvitation(Invitation invitation) {
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        String where = CanaryInvitationContentProvider.COLUMN_ID + " ==  ?";
        String[] whereArgs = new String[]{String.valueOf(invitation.id)};

        contentResolver.update(CanaryInvitationContentProvider.CONTENT_URI, createContentValuesFromInvitation(invitation), where, whereArgs);
    }

    public static void deleteInvitation(int invitationId) {
        String where = null;
        String[] whereArgs = null;
        if (invitationId != 0) {
            where = CanaryInvitationContentProvider.COLUMN_ID + " ==  ?";
            whereArgs = new String[]{String.valueOf(invitationId)};
        }

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        contentResolver.delete(CanaryInvitationContentProvider.CONTENT_URI, where, whereArgs);
    }

    public static int deleteInvitationsAtLocation(int locationId) {
        String where = null;
        String[] whereArgs = null;
        if (locationId != 0) {
            where = CanaryInvitationContentProvider.COLUMN_LOCATION_ID + " ==  ?";
            whereArgs = new String[]{String.valueOf(locationId)};
        }

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        return contentResolver.delete(CanaryInvitationContentProvider.CONTENT_URI, where, whereArgs);
    }

    public static List<Invitation> getInvitationsAtLocation(int locationId) {
        List<Invitation> invitations = new ArrayList<>();

        String where = CanaryInvitationContentProvider.COLUMN_LOCATION_ID + " ==  ?";
        String[] whereArgs = new String[]{String.valueOf(locationId)};

        Cursor cursor = CanaryApplication.getContext().getContentResolver().query(CanaryInvitationContentProvider.CONTENT_URI, null, where, whereArgs, null);

        while (cursor.moveToNext()) {
            invitations.add(getInvitationFromCursor(cursor));
        }

        cursor.close();

        return invitations;
    }

    public static ContentValues createContentValuesFromInvitation(Invitation invitation) {
        ContentValues values = new ContentValues();

        values.put(CanaryInvitationContentProvider.COLUMN_EMAIL, invitation.email);
        values.put(CanaryInvitationContentProvider.COLUMN_FIRST_NAME, invitation.firstName);
        values.put(CanaryInvitationContentProvider.COLUMN_ID, invitation.id);
        values.put(CanaryInvitationContentProvider.COLUMN_INVITER, invitation.inviter == null ?  "" : invitation.inviter);
        values.put(CanaryInvitationContentProvider.COLUMN_LAST_NAME, invitation.lastName);
        values.put(CanaryInvitationContentProvider.COLUMN_LOCATION_ID, Utils.getIntFromResourceUri(invitation.locationUri));
        values.put(CanaryInvitationContentProvider.COLUMN_PHONE, invitation.phone == null ?  "" : invitation.phone);
        values.put(CanaryInvitationContentProvider.COLUMN_STATUS, invitation.status);
        values.put(CanaryInvitationContentProvider.COLUMN_USER_TYPE, invitation.userType);
        values.put(CanaryInvitationContentProvider.COLUMN_PENDING_DELETE, false);

        return values;
    }

    // *****************************************************
    // *** Get Invitation from Cursor
    // *****************************************************
    public static Invitation getInvitationFromCursor(Cursor cursor) {
        Invitation invitation = new Invitation();

        invitation.email = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_EMAIL));
        invitation.firstName = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_FIRST_NAME));
        invitation.id = cursor.getInt(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_ID));
        invitation.inviter = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_INVITER));
        invitation.lastName = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_LAST_NAME));
        int locationId = cursor.getInt(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_LOCATION_ID));
        invitation.locationUri =  Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);
        invitation.resourceUri = Utils.buildResourceUri(Constants.INVITATION_URI, invitation.id);
        invitation.phone = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_PHONE));
        invitation.status = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_STATUS));
        invitation.userType = cursor.getString(cursor.getColumnIndex(CanaryInvitationContentProvider.COLUMN_USER_TYPE));

        return invitation;
    }

    public static void setPendingDeleteStatus(int id, boolean pending) {
        ContentValues values = new ContentValues();
        values.put(CanaryInvitationContentProvider.COLUMN_PENDING_DELETE, pending);

        String where = CanaryInvitationContentProvider.COLUMN_ID + " ==  ?";
        String[] whereArgs = new String[]{String.valueOf(id)};


        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        contentResolver.update(CanaryInvitationContentProvider.CONTENT_URI, values, where, whereArgs);
    }
}
