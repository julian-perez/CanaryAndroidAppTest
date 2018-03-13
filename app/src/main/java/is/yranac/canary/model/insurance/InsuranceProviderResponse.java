package is.yranac.canary.model.insurance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 6/2/16.
 */
public class InsuranceProviderResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<InsuranceProvider> providerList;
}
