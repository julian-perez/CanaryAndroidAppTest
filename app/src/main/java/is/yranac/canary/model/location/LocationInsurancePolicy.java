package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 6/3/16.
 */
public class LocationInsurancePolicy {

    @SerializedName("insurance_policy")
    public InsurancePolicyPatch insurancePolicy;


    public LocationInsurancePolicy(InsurancePolicyPatch insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }


    public static class InsurancePolicyPatch {


        @SerializedName("policy_number")
        public String policyNumber;

        @SerializedName("share_enabled")
        public boolean shareEnabled;

        @SerializedName("insurance_provider")
        public String insuranceProvider;

    }
}

