package com.nader.intelligent.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.iot.aep.sdk.scan.manager.MZXingManager;
import com.aliyun.iot.aep.sdk.scan.manager.OnDecodeOverListener;
import com.aliyun.iot.aep.sdk.scan.utils.AlbumPathUtils;
import com.nader.intelligent.R;
import com.nader.intelligent.view.ViewScanView;

/**
 * 摄像头
 * author:zhangpeng
 * date: 2019/11/21
 */
public class ScanFragment extends Fragment {
    private static final String TAG = "FakeHomeFragment";
    private SurfaceView mCameraPreview;
    private MZXingManager mMzXingManager;
    private ViewGroup mRootView;
    private OnDecodeOverListener onDecodeOverListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_scan, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initEvent();
    }

    private void initView() {
        mCameraPreview = mRootView.findViewById(R.id.scan_preview_surface_view);
    }

    private void initEvent() {
        //初始化相机管理器
        mMzXingManager = new MZXingManager(this, mCameraPreview);
        if (onDecodeOverListener != null) {
            mMzXingManager.setOnDecodeResultListener(onDecodeOverListener);
        }
    }

    public void setOnDecodeOverListener(OnDecodeOverListener onDecodeOverListener) {
        this.onDecodeOverListener = onDecodeOverListener;
        if (mMzXingManager != null) {
            mMzXingManager.setOnDecodeResultListener(onDecodeOverListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        holdUpBackpressEvent();
        mMzXingManager.wakeup();
    }

    private void holdUpBackpressEvent() {
        if (this.getView() != null) {
            this.getView().setFocusableInTouchMode(true);
            this.getView().requestFocus();
            this.getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mMzXingManager.isShowingPicFromAlbum()) {
                            mMzXingManager.restartPreviewDecode(0);
                            return true;
                        } else {
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onPause() {
        mMzXingManager.dormant();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMzXingManager.releaseSelf();
        super.onDestroy();
    }


    public void startPickPicFromLocalAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        }
        startActivityForResult(intent, SELECT_PICTURE);
    }

    private static final int SELECT_PICTURE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            //解析图片path
            String imagePath = AlbumPathUtils.decodeImagePathFromIntent(this.getActivity(), data);
            if (imagePath != null) {
                mMzXingManager.decodefrompic(imagePath);
            }
        }
    }

    @SuppressWarnings("unused")
    public void restartPreviewDecode(int delayTime) {
        mMzXingManager.restartPreviewDecode(delayTime);
    }

    @SuppressWarnings("unused")
    public MZXingManager getMZXingManager() {
        return mMzXingManager;
    }

    public void scanAreaVerticalPosition(float scanAreaVerticalPosition) {
        if (mMzXingManager != null) {
            mMzXingManager.scanAreaVerticalPosition(scanAreaVerticalPosition);
        }
    }

    public void setViewScanView(ViewScanView viewScanView) {
        if (mMzXingManager != null) {
            mMzXingManager.setViewScanView(viewScanView);
        }
    }
}
