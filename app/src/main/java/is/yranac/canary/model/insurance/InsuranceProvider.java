package is.yranac.canary.model.insurance;

/**
 * Created by Schroeder on 6/2/16.
 */


import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import static is.yranac.canary.contentproviders.CanaryInsuranceProviderContentProvider.*;

public class InsuranceProvider {

    public static int TOTAL_PROVIDERS = 3;


    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;


    public InsuranceProvider() {

    }

    public InsuranceProvider(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(COLUMN_PROVIDER_NAME));
        this.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
    }

}
