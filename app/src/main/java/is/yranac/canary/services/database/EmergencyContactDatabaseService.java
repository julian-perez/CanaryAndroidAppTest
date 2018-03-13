package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryEmergencyContactsContentProvider;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 10/4/15.
 */
public class EmergencyContactDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "EmergencyContactDatabaseService";

    public static void insertEmergencyContact(EmergencyContact contact) {
        ContentValues contentValues = createContentValuesFromContact(contact);
        contentResolver.insert(CanaryEmergencyContactsContentProvider.CONTENT_URI, contentValues);
    }

    private static ContentValues createContentValuesFromContact(EmergencyContact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryEmergencyContactsContentProvider.COLUMN_ID, contact.id);
        contentValues.put(CanaryEmergencyContactsContentProvider.COLUMN_PHONE_NUMBER, contact.phoneNumber);
        contentValues.put(CanaryEmergencyContactsContentProvider.COLUMN_CONTACT_TYPE, contact.contactType);
        int locationId = Utils.getIntFromResourceUri(contact.locationUri);
        contentValues.put(CanaryEmergencyContactsContentProvider.COLUMN_LOCATION_ID, locationId);
        return contentValues;
    }

    public static void insertEmergencyContacts(List<EmergencyContact> emergencyContctList) {
        for (EmergencyContact emergencyContact : emergencyContctList) {
            insertEmergencyContact(emergencyContact);
        }
    }

    public static Map<EmergencyContact.ContactType, EmergencyContact> getEmergencyContacts(int locationId) {
        String where = CanaryEmergencyContactsContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};

        Cursor cursor = contentResolver.query(CanaryEmergencyContactsContentProvider.CONTENT_URI, null, where, whereArgs, null);
        List<EmergencyContact> locationList = new ArrayList<>();

        while (cursor.moveToNext()) {
            locationList.add(createContactFromContentValues(cursor));
        }
        cursor.close();

        Map<EmergencyContact.ContactType, EmergencyContact> map = new HashMap<>();

        for (EmergencyContact emergencyContact : locationList) {

            if (EmergencyContact.ContactType.ems.equalsName(emergencyContact.contactType)) {
                map.put(EmergencyContact.ContactType.ems, emergencyContact);
            }

            if (EmergencyContact.ContactType.police.equalsName(emergencyContact.contactType)) {
                map.put(EmergencyContact.ContactType.police, emergencyContact);

            }

            if (EmergencyContact.ContactType.fire.equalsName(emergencyContact.contactType)) {
                map.put(EmergencyContact.ContactType.fire, emergencyContact);

            }
        }

        return map;

    }

    private static EmergencyContact createContactFromContentValues(Cursor cursor) {
        EmergencyContact contact = new EmergencyContact();
        contact.phoneNumber = cursor.getString(cursor.getColumnIndex(CanaryEmergencyContactsContentProvider.COLUMN_PHONE_NUMBER));
        contact.contactType = cursor.getString(cursor.getColumnIndex(CanaryEmergencyContactsContentProvider.COLUMN_CONTACT_TYPE));
        contact.id = cursor.getInt(cursor.getColumnIndex(CanaryEmergencyContactsContentProvider.COLUMN_ID));
        int locationId = cursor.getInt(cursor.getColumnIndex(CanaryEmergencyContactsContentProvider.COLUMN_LOCATION_ID));
        contact.locationUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);

        return contact;
    }
}
