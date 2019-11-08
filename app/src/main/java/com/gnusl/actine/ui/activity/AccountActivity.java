package com.gnusl.actine.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.WebViewOnFinish;
import com.gnusl.actine.network.MyWebViewClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity implements WebViewOnFinish {

    private WebView webView;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();

        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(false);
        }

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        webView.setWebViewClient(new MyWebViewClient(this));
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    public void onFinish(String url) {
        if (progressHUD != null)
            progressHUD.dismiss();
    }

//    @Override
//    public void onConnectionError(int code, String message) {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        finish();
//    }
//
//    @Override
//    public void onConnectionError(ANError anError) {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        finish();
//    }
//
//    @Override
//    public void onConnectionSuccess(JSONObject jsonObject) {
//        Intent intent = new Intent();
//        intent.putExtra("paymentId",jsonObject.optJSONObject("data").optString("payment_id"));
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}