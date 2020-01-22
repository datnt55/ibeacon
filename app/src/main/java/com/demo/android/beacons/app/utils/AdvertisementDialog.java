package com.demo.android.beacons.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.demo.android.beacons.app.R;

/**
 * Created by DatNT on 8/2/2017.
 */

public class AdvertisementDialog extends DialogFragment {

    private SelectionCallBackListener listener;
    private boolean isEmptyMessage = false, showCloseIcon = false;
    private Context mContext;
    public int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    public interface SelectionCallBackListener {
        void onPositive();

        void onNegative();

    }

    //---empty constructor required
    public AdvertisementDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //hideNavigation(uiOptions);
        View view = inflater.inflate(R.layout.dialog_advertisement, container);
        initComponents(view);
        return view;
    }

//    public void hideNavigation(final int uiOptions) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            final View decorView = getDialog().getWindow().getDecorView();
//            decorView.setSystemUiVisibility(uiOptions);
//            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//                @Override
//                public void onSystemUiVisibilityChange(int visibility) {
//                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0 && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                        decorView.setSystemUiVisibility(uiOptions);
//                    }
//                }
//            });
//        }
//    }

    private void initComponents(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.img_cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void showImageClose(){
        showCloseIcon = true;
    }
    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        return new Dialog(mContext, getTheme()) {
            @Override
            public void onBackPressed() {
                return;
            }
        };
    }
}