package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.contentproviders.CanaryCustomerContentProvider;
import is.yranac.canary.contentproviders.CanaryCustomerLocationContentProvider;
import is.yranac.canary.messages.UpdateCurrentCustomer;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.customer.CustomerLocation;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.LocaleHelper;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;

/**
 * Created by Schroeder on 8/8/14.
 */
public class CustomerDatabaseService extends BaseDatabaseService {
    private static final String LOG_TAG = "CustomerDatabaseService";

    public static final String LOCALE_CHANGED = "LOCALE_CHANGED";

    public static void insertOrUpdateCustomer(Customer customer) {


        ContentValues values = createContentValuesFromCustomer(customer);

        if (updateCustomer(customer) == 0) {
            contentResolver.insert(CanaryCustomerContentProvider.CONTENT_URI, values);

        }

        Customer customer1 = CurrentCustomer.getCurrentCustomer();


        if (customer1 != null && customer1.id == customer.id) {
            TinyMessageBus.post(new UpdateCurrentCustomer());
            Locale locale = new Locale(customer.languagePreference);
            if (!LocaleHelper.getLanguage(CanaryApplication.getContext()).equals(locale.getLanguage())) {
                Intent newIntent = new Intent();
                newIntent.setAction(LOCALE_CHANGED);
                CanaryApplication.getContext().sendBroadcast(newIntent);
            }

        }

        if (customer.avatar != null) {
            AvatarDatabaseService.insertAvatar(customer.avatar);
        } else {
            AvatarDatabaseService.deleteAvatar(customer.id);
        }

    }


    public static int deleteCustomerLocationLinksAtLocation(Context context, int locationId) {
        String where = CanaryCustomerLocationContentProvider.COLUMN_LOCATION_ID + " ==  ?";
        String[] whereArgs = new String[]{String.valueOf(locationId)};

        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(CanaryCustomerLocationContentProvider.CONTENT_URI, where, whereArgs);
    }

    public static void addCustomerLocationLink(Context context, int locationId, int customerId) {
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues values = new ContentValues();

        values.put(CanaryCustomerLocationContentProvider.COLUMN_CUSTOMER_ID, customerId);
        values.put(CanaryCustomerLocationContentProvider.COLUMN_LOCATION_ID, locationId);

        contentResolver.insert(CanaryCustomerLocationContentProvider.CONTENT_URI, values);
    }


    public static void deleteCustomer(int customerId) {
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        String where = CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID + " == ?";
        String[] whereArgs = {String.valueOf(customerId)};

        contentResolver.delete(CanaryCustomerContentProvider.CONTENT_URI, where, whereArgs);
    }

    public static List<Customer> getCustomersAtLocation(int locationId) {
        List<Customer> customers = new ArrayList<>();
        String currentUserName = PreferencesUtils.getUserName();

        if (currentUserName == null)
            return customers;


        Cursor cursor = getCursorForCustomersAtLocation(locationId);

        Log.i(LOG_TAG, "Customer count = " + cursor.getCount());
        while (cursor.moveToNext()) {
            CustomerLocation customerLocation = getCustomerLocationFromCursor(cursor);

            Customer customer = getCustomerFromId(customerLocation.customer_id);
            if (customer == null) {
                // shouldn't happen now
            } else {
                if (currentUserName.equalsIgnoreCase(customer.email)) {
                    customers.add(0, customer);
                } else {
                    customers.add(customer);
                }
            }
        }
        cursor.close();

        return customers;
    }

    public static List<Customer> getCustomersAtLocationWithAvatar(int locationId) {
        List<Customer> customers = new ArrayList<>();
        String currentUserName = PreferencesUtils.getUserName();

        if (currentUserName == null)
            return customers;


        Cursor cursor = getCursorForCustomersAtLocation(locationId);

        while (cursor.moveToNext()) {
            CustomerLocation customerLocation = getCustomerLocationFromCursor(cursor);

            Customer customer = getCustomerFromId(customerLocation.customer_id);
            if (customer != null) {

                customer.avatar = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);
                if (currentUserName.equalsIgnoreCase(customer.email)) {
                    customers.add(0, customer);
                } else {
                    customers.add(customer);
                }
            }
        }
        cursor.close();

        return customers;
    }

    public static void insertCustomersAtLocation(List<Customer> customers) {
        // now update or insert the newCustomerList
        for (Customer customer : customers) {
            insertOrUpdateCustomer(customer);
        }

    }

    public static Cursor getCursorForCustomersAtLocation(int locationId) {
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        String where = CanaryCustomerLocationContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};

        return contentResolver.query(CanaryCustomerLocationContentProvider.CONTENT_URI, null, where, whereArgs, CanaryCustomerLocationContentProvider.COLUMN_CUSTOMER_ID + " ASC");
    }

    public static int updateCustomer(Customer customer) {

        String where = CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID + " == ?";

        String[] whereArgs = {String.valueOf(customer.id)};
        ContentValues values = createContentValuesFromCustomer(customer);

        return contentResolver.update(CanaryCustomerContentProvider.CONTENT_URI, values, where, whereArgs);
    }

    public static void updateCustomerLocation(String locationUri) {
        String newLocation = locationUri == null ? "" : locationUri;
        ContentValues values = new ContentValues();

        values.put(CanaryCustomerContentProvider.COLUMN_CURRENT_LOCATION, newLocation);


        Date now = DateUtil.getCurrentTime();
        String locationChangeTime = DateUtil.convertDateToApiString(now);
        values.put(CanaryCustomerContentProvider.COLUMN_LAST_LOCATION_CHANGE, locationChangeTime);

        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer == null)
            return;

        int customerId = customer.id;
        String where = CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID + " == ?";

        String[] whereArgs = {String.valueOf(customerId)};
        ContentResolver contentResolver = CanaryApplication.getContext()
                .getContentResolver();
        contentResolver.update(CanaryCustomerContentProvider.CONTENT_URI, values, where, whereArgs);

        TinyMessageBus.post(new UpdateCurrentCustomer());
    }

    // *****************************************************
    // *** Get Customer from Id
    // *****************************************************
    public static Customer getCustomerFromId(int id) {

        String where = CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID + " == ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = contentResolver.query(CanaryCustomerContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return null;

        Customer customer = null;

        if (cursor.moveToFirst()) {
            customer = getCustomerFromCursor(cursor);
        }

        cursor.close();

        return customer;

    }

    // *****************************************************
    // *** Get Customer from Uri
    // *****************************************************
    public static Customer getCustomerFromUri(String uri) {

        String where = CanaryCustomerContentProvider.COLUMN_RESOURCE_URI + " == ?";
        String[] whereArgs = {uri};
        Cursor cursor = contentResolver.query(CanaryCustomerContentProvider.CONTENT_URI, null, where, whereArgs, null);


        if (cursor == null)
            return null;

        Customer customer = null;

        if (cursor.moveToFirst()) {
            customer = getCustomerFromCursor(cursor);
        }

        cursor.close();

        return customer;
    }


    // *****************************************************
    // *** Get Customer from Cursor
    // *****************************************************
    public static Customer getCustomerFromCursor(Cursor cursor) {
        Customer customer = new Customer();
        customer.id = cursor.getInt(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID));
        customer.created = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_CREATED));
        customer.currentLocation = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_CURRENT_LOCATION));
        customer.email = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_EMAIL));
        customer.firstName = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_FIRST_NAME));
        customer.lastLocationChange = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_LAST_LOCATION_CHANGE));
        customer.lastName = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_LAST_NAME));
        customer.notificationsSound = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_NOTIFICATION_SOUND));
        customer.phone = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_PHONE));
        customer.resourceUri = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_RESOURCE_URI));
        customer.username = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_USERNAME));
        customer.celsius = cursor.getInt(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_TEMPERATE_SETTING)) > 0;
        customer.dialCode = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_DIAL_CODE));
        customer.languagePreference = cursor.getString(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_LANGUAGE_PREFERENCE));
        customer.seenSharePrompt = cursor.getInt(cursor.getColumnIndex(CanaryCustomerContentProvider.COLUMN_HAS_SEEN_DATA_SHARE_PROMPT)) > 0;
        return customer;
    }

    // *****************************************************
    // *** Get CustomerLocation from Cursor
    // *****************************************************
    public static CustomerLocation getCustomerLocationFromCursor(Cursor cursor) {
        CustomerLocation customerLocation = new CustomerLocation();

        customerLocation.location_id = cursor.getInt(cursor.getColumnIndex(CanaryCustomerLocationContentProvider.COLUMN_LOCATION_ID));
        customerLocation.customer_id = cursor.getInt(cursor.getColumnIndex(CanaryCustomerLocationContentProvider.COLUMN_CUSTOMER_ID));

        return customerLocation;
    }

    /**
     * Create a ContentValues object from a customer object
     *
     * @param customer Location to convert
     * @return ContentValues object with the location information
     */
    public static ContentValues createContentValuesFromCustomer(Customer customer) {
        ContentValues values = new ContentValues();

        String created = customer.created;
        String currentLocation = customer.currentLocation;
        String email = customer.email;
        String firstName = customer.firstName;
        int id = customer.id;
        String lastLocationChange = customer.lastLocationChange;
        String lastName = customer.lastName;
        String phone = customer.phone;
        String resourceUri = customer.resourceUri;
        String username = customer.username;
        String dialCode = customer.dialCode;
        boolean celsius = customer.celsius;
        values.put(CanaryCustomerContentProvider.COLUMN_CREATED, created);
        values.put(CanaryCustomerContentProvider.COLUMN_CURRENT_LOCATION, currentLocation == null ? "" : currentLocation);
        values.put(CanaryCustomerContentProvider.COLUMN_EMAIL, email);
        values.put(CanaryCustomerContentProvider.COLUMN_FIRST_NAME, firstName);
        values.put(CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID, id);
        values.put(CanaryCustomerContentProvider.COLUMN_LAST_LOCATION_CHANGE, lastLocationChange == null ? "" : lastLocationChange);
        values.put(CanaryCustomerContentProvider.COLUMN_LAST_NAME, lastName);
        values.put(CanaryCustomerContentProvider.COLUMN_NOTIFICATION_SOUND, customer.notificationsSound);
        values.put(CanaryCustomerContentProvider.COLUMN_PHONE, phone);
        values.put(CanaryCustomerContentProvider.COLUMN_RESOURCE_URI, resourceUri);
        values.put(CanaryCustomerContentProvider.COLUMN_USERNAME, username);
        values.put(CanaryCustomerContentProvider.COLUMN_DIAL_CODE, dialCode);
        values.put(CanaryCustomerContentProvider.COLUMN_TEMPERATE_SETTING, celsius);
        values.put(CanaryCustomerContentProvider.COLUMN_PENDING_DELETE, true);
        values.put(CanaryCustomerContentProvider.COLUMN_LANGUAGE_PREFERENCE, customer.languagePreference);
        values.put(CanaryCustomerContentProvider.COLUMN_HAS_SEEN_DATA_SHARE_PROMPT, customer.seenSharePrompt);

        return values;
    }

    public static void deleteCustomerLocationLink(int locationId, int customerId) {
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        String where = CanaryCustomerLocationContentProvider.COLUMN_CUSTOMER_ID + " == ?";
        where += " AND ";
        where += CanaryCustomerLocationContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] whereArgs = {String.valueOf(customerId), String.valueOf(locationId)};
        contentResolver.delete(CanaryCustomerLocationContentProvider.CONTENT_URI, where, whereArgs);

    }


    public static void deleteCustomers() {
        contentResolver.delete(CanaryCustomerContentProvider.CONTENT_URI, null, null);
    }

    public static Customer getCurrentCustomer() {
        String userName = PreferencesUtils.getUserName();
        if (userName == null)
            return null;

        String where = CanaryCustomerContentProvider.COLUMN_EMAIL + " LIKE ?";
        String[] whereArgs = new String[]{userName};


        Cursor cursor = contentResolver.query(
                CanaryCustomerContentProvider.CONTENT_URI, null, where,
                whereArgs, null);

        if (cursor == null)
            return null;

        Customer customer = null;

        if (cursor.moveToFirst()) {
            customer = getCustomerFromCursor(cursor);
        }

        cursor.close();

        return customer;

    }
}
