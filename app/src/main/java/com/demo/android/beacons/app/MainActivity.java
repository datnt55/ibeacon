package com.demo.android.beacons.app;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.android.beacons.app.utils.AdvertisementDialog;
import com.demo.android.beacons.app.utils.AdvertisementDialog2;
import com.demo.android.beacons.app.utils.AdvertisementDialog3;
import com.demo.android.beacons.app.utils.SharePreference;
import com.franmontiel.localechanger.LocaleChanger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.demo.android.beacons.app.adapters.BeaconAdapter;
import com.demo.android.beacons.app.rxjava.RxObserver;
import com.demo.android.beacons.app.views.AbstractStateView;
import com.demo.android.beacons.app.views.ErrorView;
import com.demo.android.beacons.app.views.ScanningView;
import com.demo.android.beacons.ibeaconscanner.Beacon;
import com.demo.android.beacons.ibeaconscanner.Error;
import com.demo.android.beacons.ibeaconscanner.IBeaconScanner;

import static android.widget.Toast.LENGTH_LONG;
import static com.demo.android.beacons.app.utils.SharePreference.BEACON_1;
import static com.demo.android.beacons.app.utils.SharePreference.BEACON_2;
import static com.demo.android.beacons.app.utils.SharePreference.BEACON_3;

public class MainActivity extends AppCompatActivity implements IBeaconScanner.Callback, ErrorView.RetryClickListener
{
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.layout_state)
    LinearLayout layoutState;
    @BindView(R.id.progressBar)
    ProgressBar progressbar;
    private BeaconAdapter beaconAdapter;
    private AbstractStateView stateView;
    private boolean isInit = false;
    private SharePreference preference;
    private  AdvertisementDialog dialog;
    private AdvertisementDialog2 dialog2;
    private AdvertisementDialog3 dialog3;
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        ButterKnife.bind(this);
        preference = new SharePreference(this);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //this.startScanning();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (beaconAdapter != null)
            this.beaconAdapter.clear();


        if (isInit) {
            IBeaconScanner.getInstance().stop();
        }
    }

    //region Callback

    @Override
    public void didEnterBeacon(final Beacon beacon, int rssi)
    {
        if (!preference.isSaved(beacon.getUUID().toString()))
            preference.saveBeacon(beacon.getUUID().toString());

        this.beaconAdapter.updateBeacon(beacon, rssi);
        this.updateView(this.beaconAdapter.getItemCount(), null);
        //progressbar.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisibility(View.GONE);
                String id = preference.getBeacon(beacon.getUUID().toString());
                if (id != null && !isExistDialog()) {
                    if (id.equals(BEACON_1)) {
                        dialog = new AdvertisementDialog();
                        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
                        dialog.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(dialog, "tag1").commit();
                        Toast.makeText(MainActivity.this,beacon.getUUID().toString(),LENGTH_LONG).show();
                    }else if (id.equals(BEACON_2)) {
                        dialog2 = new AdvertisementDialog2();
                        dialog2.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
                        dialog2.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(dialog2, "tag2").commit();
                        Toast.makeText(MainActivity.this,beacon.getUUID().toString(),LENGTH_LONG).show();
                    }else if (id.equals(BEACON_3)) {
                        dialog3 = new AdvertisementDialog3();
                        dialog3.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
                        dialog3.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(dialog3, "tag3").commit();
                        Toast.makeText(MainActivity.this,beacon.getUUID().toString(),LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void didExitBeacon(final Beacon beacon)
    {
        //this.beaconAdapter.removeBeacon(beacon);
        //this.updateView(this.beaconAdapter.getItemCount(), null);
    }

    @Override
    public void monitoringDidFail(final Error error)
    {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        this.updateView(0, error);
        progressbar.setVisibility(View.GONE);
    }

    //endregion

    //region View

    private void startScanning()
    {
        this.updateView(0, null);
        progressbar.setVisibility(View.VISIBLE);
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new RxObserver<Boolean>()
                {
                    @Override
                    public void onNext(final Boolean granted)
                    {
                        if (granted)
                        {
                            // beacons enabled!
                            IBeaconScanner.getInstance()
                                    .startMonitoring(Beacon.newBuilder()
                                            .setUUID(UUID.randomUUID())
                                            .setMajor(1)
                                            .setMinor(1)
                                            .build());
                            final Beacon beacon = Beacon.newBuilder()
                                    .setUUID(UUID.randomUUID())
                                    .setMajor(1)
                                    .setMinor(1)
                                    .build();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (!preference.isSaved("2"))
//                                        preference.saveBeacon("2");
//                                    //beaconAdapter.updateBeacon(beacon, 10);
//                                    //updateView(beaconAdapter.getItemCount(), null);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            progressbar.setVisibility(View.GONE);
//                                            String id = preference.getBeacon("2");
//                                            if (id != null && !isExistDialog()) {
//                                                if (id.equals(BEACON_1)) {
//                                                    dialog = new AdvertisementDialog();
//                                                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
//                                                    dialog.setCancelable(false);
//                                                    getSupportFragmentManager().beginTransaction().add(dialog, "tag1").commit();
//                                                }else if (id.equals(BEACON_2)) {
//                                                     dialog2 = new AdvertisementDialog2();
//                                                    dialog2.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
//                                                    dialog2.setCancelable(false);
//                                                    getSupportFragmentManager().beginTransaction().add(dialog2, "tag2").commit();
//                                                }else if (id.equals(BEACON_3)) {
//                                                    dialog3 = new AdvertisementDialog3();
//                                                    dialog3.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
//                                                    dialog3.setCancelable(false);
//                                                    getSupportFragmentManager().beginTransaction().add(dialog3, "tag3").commit();
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//                            },1000);
//                            beaconAdapter.updateBeacon(beacon, 10);
//                            updateView(beaconAdapter.getItemCount(), null);
//                            //progressbar.setVisibility(View.GONE);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    AdvertisementDialog dialog = new AdvertisementDialog();
//                                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.NoTitleDialog);
//                                    dialog.setCancelable(false);
//                                    getSupportFragmentManager().beginTransaction().add(dialog, "tag").commit();
//                                    progressbar.setVisibility(View.GONE);
//                                }
//                            });
                        }
                        else
                        {
                            // todo
                        }
                    }
                });
    }

    private boolean isExistDialog(){
        if (dialog != null && dialog.isAdded())
            return true;
        if (dialog2 != null && dialog2.isAdded())
            return true;
        if (dialog3 != null && dialog3.isAdded())
            return true;
       return false;
    }

    private void checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        }else
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    startScanning();
                }else
                    checkPermission();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateView(final int itemCount, @Nullable Error error)
    {
        AbstractStateView view;

        if (error != null)
        {
            view = new ErrorView(this);
            ((ErrorView) view).setRetryClickListener(this);
            ((ErrorView) view).setError(error);
            this.addStateView(view);
        }
        else
        {
            if (itemCount > 0)
            {
                this.removeStateView(this.stateView);
            }
            else
            {
                view = new ScanningView(this);
                this.addStateView(view);
            }
        }
    }

    private void addStateView(final AbstractStateView view)
    {
        if (this.stateView != null)
        {
            this.removeStateView(this.stateView);
        }

        if (this.layoutState != null && view != null)
        {
            this.layoutState.addView(view);

            this.stateView = view;
        }
    }

    private void removeStateView(final AbstractStateView view)
    {
        if (this.layoutState != null)
        {
            this.layoutState.removeView(view);
        }
    }

    //endregion

    //region to

    @Override
    public void OnRetryClicked()
    {
        this.startScanning();
    }

    public void startDiscover(View view) {

        if (isInit) {
            IBeaconScanner.getInstance().stop();
        }
        if (beaconAdapter != null){
            beaconAdapter.clear();
        }
        if (beaconAdapter == null) {
            beaconAdapter = new BeaconAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(this.beaconAdapter);
        }
        IBeaconScanner.initialize(IBeaconScanner.newInitializer(this).build());
        isInit = true;
        IBeaconScanner.getInstance().setCallback(this);
        checkPermission();
    }

    //endregion
}
