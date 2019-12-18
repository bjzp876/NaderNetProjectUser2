package com.nader.intelligent.activity.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.aliyun.iot.aep.component.scan.ScanManager;
import com.aliyun.iot.aep.component.scan.ScanPluginBean;
import com.aliyun.iot.aep.sdk.scan.manager.OnDecodeOverListener;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.nader.intelligent.R;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.fragment.ScanFragment;
import com.nader.intelligent.view.ViewScanView;

import java.util.ArrayList;

/**
 * 摄像头
 * author:zhangpeng
 * date: 2019/11/21
 */
public class ScanActivity extends BaseActivity {
    public static final int PERMISSIONS_REQUEST_CAMERA = 0x11;
    public static final String CODE = "page/scan";

    private ScanFragment mScanFragment;
    private ArrayList<ScanPluginBean> mScanPluginBeans;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(hookContext(newBase));

    }

    private Context hookContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Configuration applicationConfig = context.getApplicationContext().getResources().getConfiguration();
            Configuration contextConfig = context.getResources().getConfiguration();
            contextConfig.setLocale(applicationConfig.locale);
            return context.createConfigurationContext(contextConfig);

        } else {
            return context;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        requestCameraPermission();
    }

    private void init() {
        initArgument();
        initView();
        mScanFragment.setOnDecodeOverListener(new MyOnDecodeOverListener());
    }

    private void requestCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            //noinspection StatementWithEmptyBody
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this,
                        "打开相机失败,请去设置中添加权限", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    LinkToast.makeText(this, R.string.camera_permission_denied,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
            default:
                break;
        }
    }

    private void initArgument() {
        Intent intent = getIntent();
        try {
            //noinspection unchecked
            mScanPluginBeans = intent.getParcelableArrayListExtra(ScanManager.PLUGIN_NAME_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        String scanTipStr = getString(R.string.scan_hint_text);
        mScanFragment = (ScanFragment) getFragmentManager().findFragmentById(R.id.scan_fragment);

        ViewScanView mViewScanView = new ViewScanView(this);
        mViewScanView.setCornerBoundaryStyle(Color.parseColor("#FFFFFF"), 8f, 0.15f);
        mViewScanView.setBoundaryStyle(Color.parseColor("#CCCCCC"), 2f);
        mViewScanView.setScanStyle(ViewScanView.STYLE_HYBRID);
        mViewScanView.setScanColor(Color.parseColor("#1f00C1DE"));
        mViewScanView.setScanTip(scanTipStr, (float) convertDp2Px(this, 40f), Color.WHITE, (int) convertDp2Px(this, 14f));

        mScanFragment.scanAreaVerticalPosition(0.5f);
        mScanFragment.setViewScanView(mViewScanView);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScanFragment.startPickPicFromLocalAlbum();
            }
        });
    }


    private void finishWithRequest(int resultCode, String data) {
        if (RESULT_OK == resultCode) {
            if (ScanManager.getInstance().executePlugin(this, data, mScanPluginBeans)) {
                return;
            }
        }
        Intent result = new Intent();
        result.putExtra("data", data);
        setResult(resultCode, result);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    class MyOnDecodeOverListener implements OnDecodeOverListener {
        @Override
        public void onDecodeSucessFormPic(String barcode) {
            finishWithRequest(RESULT_OK, barcode);
        }

        @Override
        public void onDecodeSucess(String barcode) {
            finishWithRequest(RESULT_OK, barcode);
        }

        @Override
        public void onDecodeFailFromPic() {
            finishWithRequest(RESULT_CANCELED, getString(R.string.scan_no_bar_code));
        }

        @Override
        public void onDecodeFail() {
            finishWithRequest(RESULT_CANCELED, getString(R.string.scan_decode_failed));
        }
    }


    public static double convertDp2Px(Context context, float count) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, count, context.getResources()
                .getDisplayMetrics());
    }
}
