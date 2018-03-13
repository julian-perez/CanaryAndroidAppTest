package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

import is.yranac.canary.contentproviders.CanarySubscriptionContentProvider;
import is.yranac.canary.messages.ServicePlanUpdated;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.subscription.Billing;
import is.yranac.canary.model.subscription.Flags;
import is.yranac.canary.model.subscription.ServiceProfile;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;

/**
 * Created by Schroeder on 5/5/15.
 */

public class SubscriptionPlanDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "SubscriptionPlanDatabaseService";

    public static void insertSubscriptionTypes(Subscription subscription) {

        Subscription oldSubscription = getServicePlanForLocation(subscription.locationId);
        checkPlanChanges(oldSubscription, subscription);
        ContentValues contentValues = createContentValuesFromPlanType(subscription);
        contentResolver.insert(CanarySubscriptionContentProvider.CONTENT_URI, contentValues);
    }

    private static void checkPlanChanges(Subscription oldSubscription, Subscription subscription) {


        if (subscription == null)
            return;

        if (oldSubscription == null) {
            EntryDatabaseService.allValidEntriesAreInDatabase(subscription.locationId, Entry.SHOWING_ALL, false);
            EntryDatabaseService.allValidEntriesAreInDatabase(subscription.locationId, Entry.SHOWING_AWAY_MODE, false);
            EntryDatabaseService.allValidEntriesAreInDatabase(subscription.locationId, Entry.SHOWING_FLAGGED, false);
            TinyMessageBus.post(new ServicePlanUpdated());
            return;
        }

        if (oldSubscription.onTrial && (!subscription.onTrial && !subscription.hasMembership)) {
            LocationDatabaseService.setTrailExpired(subscription.locationId, true);
        }


        if (oldSubscription.onTrial != subscription.onTrial || oldSubscription.hasMembership != subscription.hasMembership) {
            EntryDatabaseService.allValidEntriesAreInDatabase(subscription.locationId, Entry.SHOWING_ALL, false);
            EntryDatabaseService.allValidEntriesAreInDatabase(subscription.locationId, Entry.SHOWING_AWAY_MODE, false);
            EntryDatabaseService.allValidEntriesAreInDatabase(subscription.locationId, Entry.SHOWING_FLAGGED, false);
        }

        TinyMessageBus.post(new ServicePlanUpdated());

    }


    public static Date oldestAllowableNonStarredSubscriptionDate(int location) {
        Calendar cal = DateUtil.getCalanderInstance();

        Subscription subscription = getServicePlanForLocation(location);

        cal.add(Calendar.HOUR, -1 * (subscription.currentServiceProfile.timeLineLength));

        return new Date(cal.getTimeInMillis());
    }

    private static ContentValues createContentValuesFromPlanType(Subscription subscription) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanarySubscriptionContentProvider.COLUMN_TRIAL, subscription.onTrial);
        contentValues.put(CanarySubscriptionContentProvider.COLUMN_MEMBERSHIP, subscription.hasMembership);
        contentValues.put(CanarySubscriptionContentProvider.COLUMN_LOCATION_ID, subscription.locationId);
        contentValues.put(CanarySubscriptionContentProvider.COLUMN_C_STAT_OVERRIDE, subscription.currentServiceProfile.csatOverride);

        contentValues.put(CanarySubscriptionContentProvider.COLUMN_TIMELINE_LENGTH, subscription.currentServiceProfile.timeLineLength);
        if (subscription.currentServiceProfile.expiresOn != null) {
            contentValues.put(CanarySubscriptionContentProvider.COLUMN_EXPIRES_ON, subscription.currentServiceProfile.expiresOn.getTime());
        }

        Flags flags = subscription.flags;

        if (flags != null) {
            contentValues.put(CanarySubscriptionContentProvider.COLUMN_EMPLOYEE, flags.canaryEmployee);
            contentValues.put(CanarySubscriptionContentProvider.COLUMN_LEGACY_FREE, flags.legacyFreePlan);
        }

        if (subscription.billing != null) {
            contentValues.put(CanarySubscriptionContentProvider.COLUMN_CURRENCY, subscription.billing.currency);
            contentValues.put(CanarySubscriptionContentProvider.COLUMN_PRICE, subscription.billing.price);
            contentValues.put(CanarySubscriptionContentProvider.COLUMN_PAYMENT_PERIOD, subscription.billing.period);
        }

        return contentValues;
    }


    public static Subscription getNullableServicePlanForLocation(int location) {

        String where = CanarySubscriptionContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(location)};
        Cursor cursor = contentResolver.query(CanarySubscriptionContentProvider.CONTENT_URI, null, where, whereArgs, null);
        Subscription servicePlan = null;

        if (cursor != null && cursor.moveToFirst()) {
            servicePlan = createServicePlanFromCursor(cursor);
        }

        return servicePlan;
    }

    public static Subscription getServicePlanForLocation(int location) {

        Subscription servicePlan = getNullableServicePlanForLocation(location);

        if (servicePlan == null) {
            servicePlan = fakeServicePlan();
            servicePlan.locationId = location;
        }

        return servicePlan;
    }

    private static Subscription fakeServicePlan() {
        Subscription servicePlan = new Subscription();
        servicePlan.hasMembership = false;
        servicePlan.onTrial = false;
        servicePlan.currentServiceProfile = new ServiceProfile();
        servicePlan.currentServiceProfile.expiresOn = new Date();
        servicePlan.currentServiceProfile.timeLineLength = 30;
        return servicePlan;
    }

    public static Subscription createServicePlanFromCursor(Cursor cursor) {
        Subscription servicePlan = new Subscription();
        servicePlan.locationId = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_LOCATION_ID));
        servicePlan.hasMembership = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_MEMBERSHIP)) > 0;
        servicePlan.onTrial = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_TRIAL)) > 0;
        servicePlan.currentServiceProfile = new ServiceProfile();
        servicePlan.currentServiceProfile.csatOverride = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_C_STAT_OVERRIDE)) > 0;
        servicePlan.currentServiceProfile.expiresOn = new Date(cursor.getLong(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_EXPIRES_ON)));
        servicePlan.currentServiceProfile.timeLineLength = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_TIMELINE_LENGTH));

        String currency = cursor.getString(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_CURRENCY));

        if (currency != null) {
            servicePlan.billing = new Billing();
            servicePlan.billing.price = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_PRICE));
            servicePlan.billing.currency = currency;
            servicePlan.billing.period = cursor.getString(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_PAYMENT_PERIOD));
        }


        Flags flags = new Flags();

        flags.canaryEmployee = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_EMPLOYEE)) > 0;
        flags.legacyFreePlan = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionContentProvider.COLUMN_LEGACY_FREE)) > 0;
        servicePlan.flags = flags;
        return servicePlan;
    }

    public static Subscription getServicePlanForCurrentLocation() {
        return getServicePlanForLocation(UserUtils.getLastViewedLocationId());
    }

    public static void setExpirationDate(Date nextBillingDate, int locationId) {
        if (nextBillingDate == null)
            return;

        ContentValues contentValues = new ContentValues();
        contentValues.put(CanarySubscriptionContentProvider.COLUMN_EXPIRES_ON, nextBillingDate.getTime());
        String where = CanarySubscriptionContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};
        contentResolver.update(CanarySubscriptionContentProvider.CONTENT_URI, contentValues, where, whereArgs);

    }
}
