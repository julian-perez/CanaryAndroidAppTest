package is.yranac.canary.util;

/**
 * Created by sergeymorozov on 10/8/15.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class JSONUtil {

    static Gson gson;

    static Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().create();
        return gson;
    }

    public static <T> T  getObject(String jsonString, Type typeOfT) {
        return getGson().fromJson(jsonString, typeOfT);
    }

    public static String getJSONString(Object object) {
        return getGson().toJson(object);
    }

    public static final <T> List<T> getList(final Class<T[]> clazz, final String json) {
        final T[] jsonToObject = new Gson().fromJson(json, clazz);

        return Arrays.asList(jsonToObject);
    }
}