package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.clip.ClipsResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Schroeder on 8/11/14.
 */
public class ClipAPIService {

    private static final String LOG_TAG = "ClipAPIService";

    public static void getClips(long entryId, Callback<ClipsResponse> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ClipService clipService = restAdapter.create(ClipService.class);
        clipService.getClips(entryId, 0, callback);
    }

    public interface ClipService {
        @GET(Constants.CLIPS_URI)
        void getClips(
                @Query("entry") long entryId,
                @Query("limit") int limit,
                Callback<ClipsResponse> callback
        );
    }
}
