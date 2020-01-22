package com.demo.android.beacons.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.demo.android.beacons.app.utils.LocaleHelper;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    public void selectEnglish(View view) {
        //LocaleHelper.setLocale(getApplicationContext(),"en", "US");
        //recreate();
        LocaleChanger.setLocale((new Locale("en", "US")));
        startDiscover();
        //ActivityRecreationHelper.recreate(this, true);
    }


    public void selectJapanese(View view) {
        LocaleChanger.setLocale((new Locale("ja", "JP")));
        //ActivityRecreationHelper.recreate(this, true);
        startDiscover();
    }

    private void startDiscover(){
        startActivity(new Intent(SelectLanguageActivity.this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        //ActivityRecreationHelper.onDestroy(this);
        super.onDestroy();
    }
}
