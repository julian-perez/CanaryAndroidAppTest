package is.yranac.canary.util;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;

public class LocaleHelper {

    public static Context setLocale(Context context) {
        return updateResources(context, getLanguage(context));
    }

    public static Context setNewLocale(Context context, String language) {
        return updateResources(context, language);
    }

    public static String getLanguage(Context context) {
        Customer customer = CurrentCustomer.getCurrentCustomer();

        if (customer == null) {
            return getLocale(context.getResources()).getLanguage();
        }

        return customer.languagePreference;
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }

    public static String getDisplayLanguage(Context context) {
        return getDisplayLanguage(context, new Locale(getLanguage(context)));
    }

    public static String getDisplayLanguage(Context context, Locale locale) {
        String localeName = locale.getDisplayLanguage(new Locale(getLanguage(context)));
        return localeName.substring(0, 1).toUpperCase() + localeName.substring(1).toLowerCase();
    }
}
