package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkAuth;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.VkInfo;

public class VkAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_auth);

        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar!=null) {
            supportActionBar.hide();
        }
        webViewNavigate();
    }

    private void webViewNavigate() {
        final VkAuth vkAuth = new VkAuth(VkInfo.getAppId());
        vkAuth.setScope("messages");
        final WebView webView = (WebView) findViewById(R.id.vk_auth_web_view);
        webView.loadUrl(vkAuth.getUrl());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(final WebView view, final String url) {
                final String webViewUrl = webView.getUrl();
                if (webViewUrl.contains("access_token")) {
                    VkAuth.parseUrl(webViewUrl);
                    finish();
                }
                super.onPageFinished(view, url);
            }
        });
    }
}
