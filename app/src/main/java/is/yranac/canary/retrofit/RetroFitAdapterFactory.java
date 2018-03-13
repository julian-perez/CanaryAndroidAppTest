package is.yranac.canary.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by cumisnic on 8/4/14.
 */
public class RetroFitAdapterFactory {
    private static final String LOG_TAG = "RetroFitAdapterFactory";

    public static RestAdapter getNewDefaultAdapter(final boolean authenticated) {
        return getNewDefaultAdapter(authenticated, true, false);
    }

    /**
     * New RestAdapter that will eventually be replacing all retrofit adapters.
     * Reason being with the old retrofit adapter, we cannot add a resource uri
     *
     * @param authenticated
     * @return new RestAdapter
     */
    public static RestAdapter getNewDefaultAdapter(final boolean authenticated, boolean errorHandler, boolean logging) {

        RestAdapter.LogLevel logLevel = getLogLevel();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        String url = Constants.BASE_URL;


        Customer currentCustomer = CurrentCustomer.getCurrentCustomer();
        final String languagePref =
                currentCustomer != null ? currentCustomer.languagePreference : null;

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(url)
                .setRequestInterceptor(
                        new RequestInterceptor() {
                            @Override
                            public void intercept(final RequestFacade request) {
                                if (!StringUtils.isNullOrEmpty(languagePref))
                                    request.addHeader("Accept-Language", languagePref);

                                if (authenticated) {
                                    request.addHeader("Authorization", "Bearer " + KeyStoreHelper.getToken());
                                }
                            }
                        })
                .setClient(CanaryRetrofitClient.getClient(PreferencesUtils.trustsConnection()))
                .setConverter(new GsonConverter(gson));

        if (logging)
            builder.setLogLevel(logLevel);


        if (errorHandler)
            builder.setErrorHandler(new RetrofitErrorHandler());

        return builder.build();
    }

    private static String getLanguagePref() {
        String jsonString = Utils.loadJSONFromRawFile(R.raw.locale);

        Type type = new TypeToken<ArrayList<CountryCode>>() {
        }.getType();
        List<CountryCode> locales = new Gson().fromJson(jsonString, type);
        Locale defaultLocale = Locale.getDefault();
        String languagePref = null;
        for (CountryCode countryCode : locales) {
            if (countryCode.code.equalsIgnoreCase(defaultLocale.getLanguage()))
                languagePref = defaultLocale.getLanguage();
        }

        if (languagePref == null) {
            languagePref = Locale.ENGLISH.getLanguage();
        }
        return languagePref;
    }

    public static RestAdapter getAuthenticationAdapter() {

        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;

        String url = Constants.BASE_URL + Constants.OAUTH_VERSION;


        final String LanguagePref = getLanguagePref();
        return new RestAdapter.Builder()
                .setEndpoint(url)
                .setLogLevel(logLevel)
                .setRequestInterceptor(
                        new RequestInterceptor() {
                            @Override
                            public void intercept(final RequestFacade request) {
                                if (!StringUtils.isNullOrEmpty(LanguagePref))
                                    request.addHeader("Accept-Language", LanguagePref);
                            }
                        })
                .setClient(CanaryRetrofitClient.getClient(PreferencesUtils.trustsConnection()))
                .build();
    }

    public static RestAdapter getGooglePlacesAdapter(boolean logging) {
        RestAdapter.LogLevel logLevel = getLogLevel();
        String url = Constants.GOOGLE_PLACES_URL;

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(url)
                .setRequestInterceptor(
                        new RequestInterceptor() {
                            @Override
                            public void intercept(final RequestFacade request) {
                                request.addHeader("Content-type", "application/x-www-form-urlencoded");
                                request.addHeader("Accept", "application/x-www-form-urlencoded");
                            }
                        });
        if (logging)
            builder.setLogLevel(logLevel);
        return builder.build();
    }

    private static RestAdapter.LogLevel getLogLevel() {
        return BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
    }

    public static RestAdapter getEncryptionAdapter(final String sig, final String nonce) {
        RestAdapter.LogLevel logLevel = getLogLevel();

        String url = Constants.BASE_URL;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setLogLevel(logLevel)
                .setRequestInterceptor(
                        new RequestInterceptor() {
                            @Override
                            public void intercept(final RequestFacade request) {
                                request.addHeader("Authorization", "Bearer " + KeyStoreHelper.getToken());
                                request.addHeader("X-Canary-Sig", sig);
                                request.addHeader("X-Canary-Nonce", nonce);
                            }
                        })
                .setClient(CanaryRetrofitClient.getClient(PreferencesUtils.trustsConnection()))
                .setErrorHandler(new RetrofitErrorHandler())
                .build();

        return restAdapter;
    }

    // Deserialized API dates as UTC
    private static class DateTypeAdapter implements JsonDeserializer<Date> {
        private static final String[] DATE_FORMATS = new String[]{
                "yyyy-MM-dd'T'HH:mm:ss",
                "HH:mm"
        };


        @Override
        public synchronized Date deserialize(
                JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            for (String format : DATE_FORMATS) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return dateFormat.parse(jsonElement.getAsString());
                } catch (ParseException ignored) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }
}
