package is.yranac.canary.messages;

import is.yranac.canary.model.insurance.InsurancePolicy;

/**
 * Created by Schroeder on 6/14/16.
 */
public class UpdateInsurancePolicyComplete {
    public InsurancePolicy newInsurancePolicy;
    public int locationId;

    public UpdateInsurancePolicyComplete(InsurancePolicy newInsurancePolicy, int locationId) {

        this.newInsurancePolicy = newInsurancePolicy;
        this.locationId = locationId;
    }
}
