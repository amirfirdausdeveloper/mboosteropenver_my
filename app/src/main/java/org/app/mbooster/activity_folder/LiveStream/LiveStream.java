package org.app.mbooster.activity_folder.LiveStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;

public class LiveStream extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

        Helper.setupImageCache(this);
        Helper.CheckMaintenance(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(next);
            }
        });


        ((TextView)findViewById(R.id.toolbar_title)).setText("Mbooster Live");

        WebView myWebView = (WebView) findViewById(R.id.webView);

        //myWebView.loadUrl(html);

        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //  webview.loadData(jsonObject.getString(\"longdesc"), "text/html; charset=utf-8",null);

        myWebView.setWebViewClient(new LiveStream.MyWebViewClient());

//        WebSettings webSettings = myWebView.getSettings();

        myWebView.setWebChromeClient(new WebChromeClient());

        myWebView.getSettings().setJavaScriptEnabled(true);
//        myWebView.getSettings().setMixedContentMode(0);
        //to enable video player to use local file storage for storing decoded video files
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowContentAccess(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        //enable using MediaPlayback without user gestures.
        myWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        myWebView.getSettings().setAppCacheMaxSize(5*1024*1024);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setSaveFormData(false);
        myWebView.getSettings().setSavePassword(false);
        myWebView.getSettings().setSupportMultipleWindows(false);
        myWebView.getSettings().setEnableSmoothTransition(true);
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);



        myWebView.setOnTouchListener((v, event) -> (event.getAction() == MotionEvent.ACTION_MOVE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        try {
            myWebView.loadUrl("https://www.mbooster.my/appV2/live/live.php");
        } catch (Throwable err) {
            finish();
        }



    }

    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(next);
    }

    private class MyWebViewClient extends WebViewClient {

// @Override
//
// public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
// if (Uri.parse(url).getHost().equals(html)) {
//
// return false;
//
// }
//
//
// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//
// startActivity(intent);
//
// return true;
// }
//
// @Override
// public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
// handler.proceed(); // Ignore SSL certificate errors
// }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            try {

            } catch (Throwable err) {

            }
            return false;

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            if (failingUrl.startsWith("mcalls://") || failingUrl.startsWith("mailto")) {

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
            finish();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {


        }

        public void onPageFinished(WebView view, String url) {

        }

    }

}