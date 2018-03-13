package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import is.yranac.canary.contentproviders.CanarySubscriptionPricesContentProvider;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.subscription.Billing;
import is.yranac.canary.model.subscription.SubscriptionPrice;

/**
 * Created by michaelschroeder on 10/3/16.
 */

public class SubscriptionPricesDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "SubscriptionPricesDatabaseService";

    public static void insertSubscriptionPricesData(List<SubscriptionPrice> subscriptionPrices) {
        contentResolver.delete(CanarySubscriptionPricesContentProvider.CONTENT_URI, null, null);
        for (SubscriptionPrice subscriptionPrice : subscriptionPrices) {
            insertPrices(subscriptionPrice.deviceCount, "annual", subscriptionPrice.prices.annualPrices);
            insertPrices(subscriptionPrice.deviceCount, "monthly", subscriptionPrice.prices.monthlyPrices);
        }
    }

    private static void insertPrices(int deviceCount, String paymentPeriod, int prices) {
        ContentValues contentValues = contentValuesFromPrices(deviceCount, paymentPeriod, prices);
        contentResolver.insert(CanarySubscriptionPricesContentProvider.CONTENT_URI, contentValues);

    }

    private static ContentValues contentValuesFromPrices(int deviceCount, String paymentPeriod,
                                                         int price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanarySubscriptionPricesContentProvider.COLUMN_DEVICE_COUNT, deviceCount);
        contentValues.put(CanarySubscriptionPricesContentProvider.COLUMN_PRICE, price);
        contentValues.put(CanarySubscriptionPricesContentProvider.COLUMN_PAYMENT_PERIOD, paymentPeriod);
        return contentValues;
    }

    public static Billing getPriceForDevices(int devices, String period) {
        String where = CanarySubscriptionPricesContentProvider.COLUMN_DEVICE_COUNT + " == ?";
        where += " AND ";
        where += CanarySubscriptionPricesContentProvider.COLUMN_PAYMENT_PERIOD + " == ?";
        String[] whereArgs = {String.valueOf(devices), period};
        Cursor cursor = contentResolver.query(CanarySubscriptionPricesContentProvider.CONTENT_URI, null, where, whereArgs, null);


        Billing billingStatement;
        if (cursor != null) {

            if (cursor.moveToFirst()) {

                billingStatement = billingStatementFromCursor(cursor);
            } else {
                billingStatement = fakeBilling();
            }
            cursor.close();
        } else {
            billingStatement = fakeBilling();
        }


        return billingStatement;


    }

    private static Billing fakeBilling() {
        Billing billingStatement = new Billing();
        billingStatement.period = "monthly";
        billingStatement.price = 999;
        return billingStatement;
    }

    private static Billing billingStatementFromCursor(Cursor cursor) {
        Billing billingStatement = new Billing();
        billingStatement.period = cursor.getString(cursor.getColumnIndex(CanarySubscriptionPricesContentProvider.COLUMN_PAYMENT_PERIOD));
        billingStatement.price = cursor.getInt(cursor.getColumnIndex(CanarySubscriptionPricesContentProvider.COLUMN_PRICE));
        return billingStatement;
    }
}
