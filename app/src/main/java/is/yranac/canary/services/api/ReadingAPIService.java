package is.yranac.canary.services.api;

import java.util.Date;

import is.yranac.canary.Constants;
import is.yranac.canary.model.reading.ReadingResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.util.DateUtil;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Schroeder on 10/21/14.
 */
public class ReadingAPIService {


    /**
     * Synchronous call to get Readings for the pst 24 hours
     *
     * @param date
     * @return Reading Response with the reading array
     */

    public static ReadingResponse getReadingsSinceDate(Date date, int deviceId) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ReadingService readingService = restAdapter.create(ReadingService.class);
        String createdRange = DateUtil.convertDateToApiString(date) + ", " + DateUtil.convertDateToApiString(DateUtil.getCurrentTime());
        return readingService.getPastDayReadings(createdRange, deviceId, "0", "10m");
    }

    /**
     * Synchronous call to get Readings for the pst 24 hours
     *
     * @param resourceUri
     * @return Reading Response with the reading array
     */
    public static ReadingResponse getResourceByUri(String resourceUri) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ReadingService readingService = restAdapter.create(ReadingService.class);
        return readingService.getReadingByUri(resourceUri.substring(1));
    }


    public interface ReadingService {

        @GET(Constants.READING_URI)
        ReadingResponse getPastDayReadings(
                @Query("created__range") String date,
                @Query("device") int device,
                @Query("limit") String limit,
                @Query("resolution") String resolution);

        @GET("/{resource_uri}")
        ReadingResponse getReadingByUri(
                @Path(value = "resource_uri", encode = false)
                        String resourceUri
        );
    }


}
