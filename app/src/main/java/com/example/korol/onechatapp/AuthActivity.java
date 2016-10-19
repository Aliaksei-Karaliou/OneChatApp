package com.example.korol.onechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.korol.onechatapp.logic.vk.VkInfo;

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onPostResume() {
        AuthIndormation authIndormation = new AuthIndormation();
        super.onPostResume();
        if (AuthIndormation.isVkAuth()){
            Toast.makeText(this, VkInfo.getUserId(), Toast.LENGTH_SHORT).show();
        }
    }

    public void VkAuthClick(View view) {
        startActivity(new Intent(this, VkAuthActivity.class));
    }
}
