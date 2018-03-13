package is.yranac.canary.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import is.yranac.canary.R;

public class GenericWebviewFragment extends StackFragment {

    public static GenericWebviewFragment newInstance(String url, int title) {
        GenericWebviewFragment genericWebviewFragment = new GenericWebviewFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("url", url);
        genericWebviewFragment.setArguments(args);
        return genericWebviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        WebSettings wbset = webView.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbset.setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClient());
        String url = getArguments().getString("url");
        webView.loadUrl(url);

        view.findViewById(R.id.x_button).setVisibility(View.GONE);

        TextView titleTextView = (TextView) view.findViewById(R.id.header_title_text_view);
        titleTextView.setText(getArguments().getInt("title"));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }
}


