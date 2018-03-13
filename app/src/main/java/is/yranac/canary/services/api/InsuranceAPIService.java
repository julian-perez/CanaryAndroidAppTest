package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.insurance.InsuranceProviderResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.InsuranceDatabaseService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;

/**
 * Created by Schroeder on 8/8/14.
 */
public class InsuranceAPIService {


    public static InsuranceProviderResponse getInsuranceProviders() throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        InsuranceService insuranceService = restAdapter.create(InsuranceService.class);
        InsuranceProviderResponse response = insuranceService.getInsuranceProviders();
        InsuranceDatabaseService.insertInsuranceProviders(response.providerList);
        return response;
    }

    public interface InsuranceService {

        @GET(Constants.INSURANCE_URI)
        InsuranceProviderResponse getInsuranceProviders();

    }
}
