package is.yranac.canary.retrofit;

import android.annotation.SuppressLint;

import retrofit.client.Client;

/**
 * Created by sergeymorozov on 9/15/15.
 */
@SuppressLint("DefaultLocale")
public class CanaryRetrofitClient {

    public static Client getClient(boolean pinning){
        return DefaultRetrofitClient.getClient(pinning);
    }
}