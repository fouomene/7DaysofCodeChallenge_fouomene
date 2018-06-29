package net.freelancertech.journal.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 *
 * A placeholder fragment containing a simple view.
 */
public class WebViewFragment extends Fragment {

    private static final String LOG_TAG = WebViewFragment.class.getSimpleName();

    public static final String CODE_HTML = "HTML";
    private WebView mWebView;
    private String mCodeHTML;
    private int mNumTab ;


    public WebViewFragment() {
    }

    @SuppressLint("ValidFragment")
    public WebViewFragment(String codeHTML, int numTab) {
        mCodeHTML = codeHTML;
        mNumTab = numTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(WebViewFragment.CODE_HTML)) {
            // probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mCodeHTML = savedInstanceState.getString(WebViewFragment.CODE_HTML);
            Log.d(LOG_TAG, "CODE HTML Save =" + mCodeHTML);
        }

        View rootView = inflater.inflate(net.freelancertech.journal.app.R.layout.fragment_web_view, container, false);

        mWebView=(WebView)rootView.findViewById(net.freelancertech.journal.app.R.id.webView);



        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        final Activity activity = getActivity();
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet) + description, Toast.LENGTH_SHORT).show();
            }
        });


       /* if (mNumTab == 2 ) { mWebView.setEnabled(false);}*/
        mWebView.loadData(mCodeHTML, "text/html", null);
       // else mWebView.loadData("<html><body>Verifiez que votre connexion Internet est active.</body></html>", "text/html", null);


        return rootView;

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        outState.putString(CODE_HTML, mCodeHTML);
        super.onSaveInstanceState(outState);
    }
}
