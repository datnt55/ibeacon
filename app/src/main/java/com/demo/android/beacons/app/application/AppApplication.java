package com.demo.android.beacons.app.application;

import android.app.Application;
import android.content.res.Configuration;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by eliaslecomte on 27/09/2016.
 */

public class AppApplication extends Application
{
    public static final List<Locale> SUPPORTED_LOCALES =
            Arrays.asList(
                    new Locale("en", "US"),
                    new Locale("ja", "JP"));
    @Override
    public void onCreate()
    {
        super.onCreate();
        LocaleChanger.initialize(getApplicationContext(), SUPPORTED_LOCALES);
        // initialize LeakCanary
//        if (LeakCanary.isInAnalyzerProcess(this))
//        {
//            return;
//        }
//        LeakCanary.install(this);
//
//        // initialize Facebook Stetho
//        Stetho.initializeWithDefaults(this);

        // initialize In The Pockets iBeaconScanner
        //IBeaconScanner.initialize(IBeaconScanner.newInitializer(this).build());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
