package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.mode.ModeResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.ModeDatabaseService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;

/**
 * Created by Schroeder on 8/8/14.
 */
public class ModeAPIService {

    public static ModeResponse getModes() throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ModeService modeService = restAdapter.create(ModeService.class);

        ModeResponse modeResponse = modeService.getModes();
        ModeDatabaseService.insertModes(modeResponse.modes);

        return modeResponse;

    }

    public interface ModeService {
        @GET(Constants.MODES_URI)
        void getModes(Callback<ModeResponse> modeGetCallback);

        @GET(Constants.MODES_URI)
        ModeResponse getModes();

    }
}
