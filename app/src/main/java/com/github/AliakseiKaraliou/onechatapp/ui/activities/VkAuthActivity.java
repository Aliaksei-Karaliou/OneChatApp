package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.Constants;
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
        vkAuth.setScope(Constants.Params.MESSAGES);
        final WebView webView = (WebView) findViewById(R.id.vk_auth_web_view);
        final String url = vkAuth.getUrl();
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(final WebView view, final String url) {
                final String webViewUrl = webView.getUrl();
                if (webViewUrl.contains(Constants.Params.ACCESS_TOKEN)) {
                    VkAuth.parseUrl(webViewUrl);
                    finish();
                }
                else if (webViewUrl.contains(Constants.Params.JOIN)){
                    finish();
                    Toast.makeText(VkAuthActivity.this, R.string.registartion_unavailable, Toast.LENGTH_SHORT).show();
                }
                super.onPageFinished(view, url);
            }
        });
    }
}
