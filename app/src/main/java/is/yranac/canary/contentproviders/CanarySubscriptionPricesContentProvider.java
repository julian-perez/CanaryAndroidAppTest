package is.yranac.canary.contentproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

import is.yranac.canary.Constants;

/**
 * Created by Schroeder on 10/21/14.
 */

public class CanarySubscriptionPricesContentProvider extends CanaryContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanarySubscriptionPricesContentProvider";

    public static final String TABLE_SUBSCRIPTION_PRICES = "subscription_prices_table";

    public static final String READING_ID = "_id";
    public static final String COLUMN_DEVICE_COUNT = "device_count";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PAYMENT_PERIOD = "payment_period";

    public static final String DATABASE_CREATE_SUBSCRIPTION_PRICES = "CREATE TABLE " + TABLE_SUBSCRIPTION_PRICES + "("
            + READING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DEVICE_COUNT + " INTEGER, "
            + COLUMN_PRICE + " INTEGER, "
            + COLUMN_PAYMENT_PERIOD + " TEXT, "
            + " UNIQUE (" + COLUMN_DEVICE_COUNT + " , " + COLUMN_PAYMENT_PERIOD
            + ") ON CONFLICT REPLACE "
            + ");";

    private static final String BASE_PATH = "subscriptionpricesdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_SUBSCRIPTION_PRICES + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/readings";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/reading";


    private static final int SUBSCRIPTION_PRICES = 10;


    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_SUBSCRIPTION_PRICES, BASE_PATH, SUBSCRIPTION_PRICES);
    }


    @Override
    public String[] getProjectionColumns() {
        return new String[]{READING_ID, COLUMN_PAYMENT_PERIOD,
                COLUMN_DEVICE_COUNT, COLUMN_PRICE};
    }

    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_SUBSCRIPTION_PRICES;
    }

    @Override
    public String getBasePath() {
        return BASE_PATH;
    }

    @Override
    public Uri getContentUrl() {
        return CONTENT_URI;
    }

    @Override
    public UriMatcher getURIMatcher() {
        return sURIMatcher;
    }

    @Override
    public String getTableName() {
        return TABLE_SUBSCRIPTION_PRICES;
    }


}
