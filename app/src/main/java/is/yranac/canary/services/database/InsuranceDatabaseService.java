package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.contentproviders.CanaryInsurancePolicyContentProvider;
import is.yranac.canary.contentproviders.CanaryInsuranceProviderContentProvider;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.insurance.InsuranceProvider;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 6/2/16.
 */
public class InsuranceDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "InsuranceDatabaseService";

    public static List<InsuranceProvider> getAllProviders() {
        List<InsuranceProvider> insuranceProviders = new ArrayList<>();

        Cursor cursor = contentResolver.query(CanaryInsuranceProviderContentProvider.CONTENT_URI, null, null, null, null);


        if (cursor == null)
            return insuranceProviders;


        if (cursor.moveToFirst()) {
            do {
                insuranceProviders.add(new InsuranceProvider(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();


        return insuranceProviders;
    }

    public static void insertInsuranceProviders(List<InsuranceProvider> providerList) {

        for (InsuranceProvider insuranceProvider : providerList) {
            ContentValues contentValues = contentValuesFromInsuranceProvider(insuranceProvider);
            contentResolver.insert(CanaryInsuranceProviderContentProvider.CONTENT_URI, contentValues);
        }

    }

    public static ContentValues contentValuesFromInsuranceProvider(InsuranceProvider insuranceProvider) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryInsuranceProviderContentProvider.COLUMN_ID, insuranceProvider.id);
        contentValues.put(CanaryInsuranceProviderContentProvider.COLUMN_PROVIDER_NAME, insuranceProvider.name);
        return contentValues;
    }

    public static void insertInsurancePolicy(InsurancePolicy insurancePolicy, int locationId) {
        if (insurancePolicy == null)
            return;

        ContentValues values = contentValuesFromInsurancePolicy(insurancePolicy, locationId);

        String where = CanaryInsurancePolicyContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};
        if (contentResolver.update(CanaryInsurancePolicyContentProvider.CONTENT_URI, values, where, whereArgs) == 0) {
            contentResolver.insert(CanaryInsurancePolicyContentProvider.CONTENT_URI, values);
        }
    }


    public static ContentValues contentValuesFromInsurancePolicy(InsurancePolicy insuranceProvider, int locationId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryInsurancePolicyContentProvider.COLUMN_LOCATION_ID, locationId);
        contentValues.put(CanaryInsurancePolicyContentProvider.COLUMN_PPROVIDER_NAME, insuranceProvider.insuranceProvider.name);
        contentValues.put(CanaryInsurancePolicyContentProvider.COLUMN_WILL_SHARE_DATA, insuranceProvider.shareEnabled);
        contentValues.put(CanaryInsurancePolicyContentProvider.COLUMN_POLICY_NUMBER, insuranceProvider.policyNumber);
        contentValues.put(CanaryInsurancePolicyContentProvider.COLUMN_PROVIDER_ID, insuranceProvider.insuranceProvider.id);
        return contentValues;
    }


    public static InsurancePolicy getInsurancePolicy(int locationId) {

        InsurancePolicy insurancePolicy = null;
        String where = CanaryInsurancePolicyContentProvider.COLUMN_LOCATION_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};

        Cursor cursor = contentResolver.query(CanaryInsurancePolicyContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return insurancePolicy;

        if (cursor.moveToFirst()) {
            insurancePolicy = new InsurancePolicy(cursor);
        }

        cursor.close();

        return insurancePolicy;
    }

    public static InsuranceProvider insuranceProvider(String resourceUri) {
        InsuranceProvider insuranceProvider = null;


        String where = CanaryInsuranceProviderContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(Utils.getIntFromResourceUri(resourceUri))};

        Cursor cursor = contentResolver.query(CanaryInsuranceProviderContentProvider.CONTENT_URI, null, where, whereArgs, null);


        if (cursor == null)
            return insuranceProvider;


        if (cursor.moveToFirst()) {
            do {
                insuranceProvider = new InsuranceProvider(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();


        return insuranceProvider;
    }
}