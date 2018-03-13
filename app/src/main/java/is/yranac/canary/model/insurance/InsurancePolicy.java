package is.yranac.canary.model.insurance;

/**
 * Created by Schroeder on 6/2/16.
 */


import android.database.Cursor;

import com.google.gson.annotations.SerializedName;


import static is.yranac.canary.contentproviders.CanaryInsurancePolicyContentProvider.*;


public class InsurancePolicy {


    @SerializedName("policy_number")
    public String policyNumber;

    @SerializedName("share_enabled")
    public boolean shareEnabled;

    @SerializedName("insurance_provider")
    public InsuranceProvider insuranceProvider;


    public InsurancePolicy() {

    }

    public InsurancePolicy(Cursor cursor) {
        this.policyNumber = cursor.getString(cursor.getColumnIndex(COLUMN_POLICY_NUMBER));
        if (policyNumber == null)
            policyNumber = "";

        this.shareEnabled = cursor.getInt(cursor.getColumnIndex(COLUMN_WILL_SHARE_DATA)) > 0;
        this.insuranceProvider = new InsuranceProvider();
        this.insuranceProvider.name = cursor.getString(cursor.getColumnIndex(COLUMN_PPROVIDER_NAME));
        this.insuranceProvider.id = cursor.getInt(cursor.getColumnIndex(COLUMN_PROVIDER_ID));
    }
}
