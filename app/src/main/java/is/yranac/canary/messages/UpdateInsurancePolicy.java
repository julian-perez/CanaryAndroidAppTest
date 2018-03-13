package is.yranac.canary.messages;

import is.yranac.canary.model.location.LocationInsurancePolicy.InsurancePolicyPatch;

/**
 * Created by Schroeder on 6/3/16.
 */
public class UpdateInsurancePolicy {
    public int id;
    public InsurancePolicyPatch insurancePolicy;

    public UpdateInsurancePolicy(int id, InsurancePolicyPatch insurancePolicy) {
        this.id = id;
        this.insurancePolicy = insurancePolicy;
    }
}
