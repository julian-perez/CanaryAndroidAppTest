package is.yranac.canary.ui;

/**
 * Created by Schroeder on 7/8/16.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.util.Utils;

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (Utils.isDev()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String url = prefs.getString("url", Constants.BASE_URL_ENDPOINT);
            Constants.updateEndPoint(url);
        }
    }
}