package is.yranac.canary.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.model.clip.Clip;
import is.yranac.canary.model.clip.ClipsResponse;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.Log;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

/**
 * Created by sergeymorozov on 9/15/15.
 */
@SuppressLint("DefaultLocale")
public class CanaryRetrofitClient implements Client {

    private static final String LOG_TAG = "CanaryRetrofitClient";

    public static CanaryRetrofitClient getClient(boolean pinned) {
        return new CanaryRetrofitClient();
    }

    @Override
    public Response execute(Request request) throws IOException {
        String url = request.getUrl();

        if (url.contains("clips")) {
            return clipsResponse(request);
        }
//        String filename = getFilenameFromUrl(url);


        Locale locale = Locale.getDefault();

        Log.i(LOG_TAG, "Locale " + locale.toString());
        int resourceId = getRawId(url);


        InputStream inputStream = getContext().getResources().openRawResource(resourceId);

        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
        if (mimeType == null) {
            mimeType = "application/json";
        }

        TypedInput body = new TypedInputStream(mimeType, inputStream.available(), inputStream);
        return new Response(request.getUrl(), 200, "Content from res/raw", new ArrayList<Header>(), body);
    }

    private Response clipsResponse(Request request) throws IOException {

        Uri uri = Uri.parse(request.getUrl());
        String param = uri.getQueryParameter("entry");
        Entry entry = EntryDatabaseService.getEntryFromEntryId(Integer.parseInt(param));
        if (entry == null) {
            return new Response(request.getUrl(), 400, "Cannot find entry", new ArrayList<Header>(), null);
        }
        List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(Integer.parseInt(param));
        ClipsResponse clipsResponse = new ClipsResponse();
        clipsResponse.clips = new ArrayList<>();
        for (Thumbnail thumbnail : thumbnails) {

            Log.i(LOG_TAG, String.valueOf(thumbnail.length));
            Clip clip = new Clip();
            clip.device = thumbnail.device;
            clip.start = entry.startTime;
            clip.end = new Date(entry.startTime.getTime() + thumbnail.length * 1000);
            clip.duration = thumbnail.length;
            clipsResponse.clips.add(clip);
        }

        Gson gson = new Gson();
        String json = gson.toJson(clipsResponse);
        Log.i(LOG_TAG, json);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));


        String mimeType = "application/json";


        TypedInput body = new TypedInputStream(mimeType, inputStream.available(), inputStream);

        return new Response(request.getUrl(), 200, "Content from dynamic clip response", new ArrayList<Header>(), body);

    }

    private static class TypedInputStream implements TypedInput {
        private final String mimeType;
        private final long length;
        private final InputStream stream;

        private TypedInputStream(String mimeType, long length, InputStream stream) {
            this.mimeType = mimeType;
            this.length = length;
            this.stream = stream;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        public InputStream in() throws IOException {
            return stream;
        }
    }

    private int getRawId(String url) {
        if (url.contains(Constants.LOCATIONS_URI)) {
            return R.raw.locations;
        } else if (url.contains(Constants.MODES_URI)) {
            return R.raw.modes;
        } else if (url.contains(Constants.PLAN_TYPES_URI)) {
            return R.raw.plan_type;
        } else if (url.contains(Constants.ENTRIES_URI)) {

            if (url.contains("last_modified"))
                return R.raw.empty;

            if (url.contains("location=2"))
                return R.raw.empty;

            if (url.contains("starred"))
                return R.raw.empty;

            if (!url.contains("offset=0"))
                return  R.raw.empty;

            Log.i(LOG_TAG, url);
            return  R.raw.entries;
        } else if (url.contains(Constants.READING_URI)) {
            if (url.contains("sensor_type=1")) {
                if (url.contains("device=136")) {
                    return R.raw.humidity_1;
                } else if (url.contains("device=137")) {
                    return R.raw.humidity_2;
                } else if (url.contains("device=196")) {
                    return R.raw.humidity_1;
                }
            } else if (url.contains("sensor_type=2")) {
                if (url.contains("device=136")) {
                    return R.raw.temperature_1;
                } else if (url.contains("device=137")) {
                    return R.raw.temperature_2;
                } else if (url.contains("device=196")) {
                    return R.raw.temperature_1;
                }
            } else if (url.contains("sensor_type=3")) {
                if (url.contains("device=136")) {
                    return R.raw.air_1;
                } else if (url.contains("device=137")) {
                    return R.raw.air_2;
                } else if (url.contains("device=196")) {
                    return R.raw.air_1;
                }
            }
        }
        return R.raw.empty;
    }

    private Context getContext() {
        return CanaryApplication.getContext();
    }
}