package com.example.korol.onechatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.korol.onechatapp.logic.common.AuthIndormation;
import com.example.korol.onechatapp.logic.vk.VkAuth;
import com.example.korol.onechatapp.logic.vk.VkInfo;

import java.util.Map;

public class VkAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_auth);
        getSupportActionBar().hide();
        webViewNavigate();
    }

    private void webViewNavigate() {
        final VkAuth vkAuth = new VkAuth(getResources().getInteger(R.integer.appId));
        vkAuth.setScope("messages");
        final WebView webView = (WebView) findViewById(R.id.vk_auth_web_view);
        //  webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(vkAuth.getUrl());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String webViewUrl = webView.getUrl();
                if (webViewUrl.contains("access_token")) {
                    VkInfo vkInfo = new VkInfo(webViewUrl);
                    new AuthIndormation().setVkAuth(true);
                    finish();
                }
                super.onPageFinished(view, url);
            }
        });
    }

    //  https://oauth.vk.com/blank.html#access_token=1ac55041f635b3325ae72d3125e2f409d07dde6cbd1e73910b3170a85c250990f5d90005ddaa265205212&expires_in=86400&user_id=127804881
}
