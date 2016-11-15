package com.github.AliakseiKaraliou.onechatapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.AliakseiKaraliou.onechatapp.R;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkAuth;
import com.github.AliakseiKaraliou.onechatapp.logic.vk.VkInfo;

public class VkAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_auth);
        getSupportActionBar().hide();
        webViewNavigate();
    }

    private void webViewNavigate() {
        final VkAuth vkAuth = new VkAuth(VkInfo.getAppId());
        vkAuth.setScope("messages");
        final WebView webView = (WebView) findViewById(R.id.vk_auth_web_view);
        webView.loadUrl(vkAuth.getUrl());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String webViewUrl = webView.getUrl();
                if (webViewUrl.contains("access_token")) {
                    VkInfo.setUrl(webViewUrl);
                    finish();
                }
                super.onPageFinished(view, url);
            }
        });
    }

    //  https://oauth.vk.com/blank.html#access_token=1ac55041f635b3325ae72d3125e2f409d07dde6cbd1e73910b3170a85c250990f5d90005ddaa265205212&expires_in=86400&user_id=127804881
}
