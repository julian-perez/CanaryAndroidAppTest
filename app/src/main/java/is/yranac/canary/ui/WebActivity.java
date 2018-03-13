package is.yranac.canary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

import java.util.HashMap;
import java.util.Map;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentWebViewBinding;
import is.yranac.canary.util.keystore.KeyStoreHelper;

/**
 * Created by michaelschroeder on 1/19/17.
 */

public class WebActivity extends BaseActivity implements View.OnClickListener {

    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String AUTO_LOGIN = "auto_login";


    public static Intent newInstance(Context context, String url, String title, boolean autoLogin) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(AUTO_LOGIN, autoLogin);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    private FragmentWebViewBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentWebViewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        String title = getIntent().getStringExtra(TITLE);
        if (title == null) {
            title = getString(R.string.activate_membership);
        }

        String url;

        if (action != null && action.equalsIgnoreCase(Intent.ACTION_VIEW)) {
            if (KeyStoreHelper.hasGoodOauthToken()) {
                url = Constants.autoLoginUrl(data, this);
            } else {
                url = data.toString();
            }
        } else {
            url = getIntent().getStringExtra(URL);
        }


        boolean autoLogin = getIntent().getBooleanExtra(AUTO_LOGIN, true);

        binding.headerTitleTextView.setText(title);
        binding.xButton.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }

        if (TextUtils.isEmpty(url)){
            finish();
            return;
        }


        binding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.webview.getSettings().setAppCacheEnabled(false);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(false);
        binding.webview.clearHistory();
        binding.webview.clearCache(true);


        binding.webview.clearFormData();
        binding.webview.getSettings().setSaveFormData(false);

        if (autoLogin && KeyStoreHelper.hasGoodOauthToken()) {
            Map<String, String> mapParams = new HashMap<>();
            mapParams.put("Authorization", "Bearer " + KeyStoreHelper.getToken());
            binding.webview.loadUrl(url, mapParams);
        } else {
            binding.webview.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (binding.webview.canGoBack()) {
                        binding.webview.goBack();
                    } else {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}