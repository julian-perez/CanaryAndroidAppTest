package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.EncryptionTokenResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by sergeymorozov on 10/27/15.
 */
public class EncryptionAPIService {

    public static void getEncryptionToken(String deviceSerial, Callback<EncryptionTokenResponse> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EnrcyptionService encryptionService = restAdapter.create(EnrcyptionService.class);
        encryptionService.getEncryptionToken(deviceSerial, callback);
    }



    public static void getEncryptionToken(String deviceSerial, String sig, String nonce, Callback<EncryptionTokenResponse> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getEncryptionAdapter(sig, nonce);
        EnrcyptionService encryptionService = restAdapter.create(EnrcyptionService.class);
        encryptionService.getEncryptionToken(deviceSerial, callback);
    }
    public interface EnrcyptionService {

        /**
         * Gets encryption token from the Cloud
         *
         * @param callback
         * @return
         */
        @GET(Constants.ENCRYPTION_TOKEN)
        void getEncryptionToken(
                @Query("device_serial") String deviceSerial,
                Callback<EncryptionTokenResponse> callback
        );
    }
}