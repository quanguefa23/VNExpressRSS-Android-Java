package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ReadNewsActivity extends AppCompatActivity {

    WebView webView;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        getLink();
        changURLTitle();
        setupWebView();
    }

    private void getLink() {
        link = getIntent().getStringExtra("link");
    }

    private void changURLTitle() {
        TextView urlTitleTV = findViewById(R.id.url_title);
        urlTitleTV.setText(link);
    }

    private void setupWebView() {
        webView = findViewById(R.id.webview);
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
}
