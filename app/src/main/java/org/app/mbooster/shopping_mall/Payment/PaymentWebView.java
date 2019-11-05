package org.app.mbooster.shopping_mall.Payment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.app.mbooster.R;
import org.app.mbooster.shopping_mall.PaymentFailed;
import org.app.mbooster.shopping_mall.PaymentSuccessful;

public class PaymentWebView extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        i.getExtras();
        String url = i.getStringExtra("url");

        //testing
//        url = "http://www.mbooster.my/app/shop/test/test_post.php";

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                if (url.startsWith("https://mbooster.my/app/shop/ipay88_success.php") || url.startsWith("https://www.mbooster.my/app/shop/ipay88_success.php")
//                        || url.startsWith("https://www.mbooster.my/mmspot/payment/ipay88_success.php") || url.startsWith("https://mbooster.my/mmspot/payment/ipay88_success.php") || url.startsWith("https://mbooster.my/stgmb/payment/ipay88_success.php")) {
                if (url.contains("ipay88_success.php")) {
                    // Parse further to extract function and do custom action
                    startActivity(new Intent(PaymentWebView.this, PaymentSuccessful.class));
                    finish();
//                } else if (url.startsWith("https://mbooster.my/app/shop/ipay88_failed.php") || url.startsWith("https://www.mbooster.my/app/shop/ipay88_failed.php")||
//                        url.startsWith("https://www.mbooster.my/mmspot/payment/ipay88_failed.php") ||url.startsWith("https://mbooster.my/mmspot/payment/ipay88_failed.php") || url.startsWith("https://mbooster.my/stgmb/payment/ipay88_failed.php")) {
                } else if (url.contains("ipay88_failed.php")) {
                    startActivity(new Intent(PaymentWebView.this, PaymentFailed.class));
                    finish();
                } else {
                    // Load the page via the webview
                    Log.i("url redirected" , url);
                    view.loadUrl(url);
                }

                return false;
            }
        });
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSaveFormData(false);

        webView.loadUrl(url);
    }


}
