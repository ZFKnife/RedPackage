package com.sucisoft.test;

import android.graphics.PixelFormat;
import android.view.SurfaceView;

public class HolderSurfaceView {
    private HolderSurfaceView() {
    }

    private SurfaceView mSurfaceView;
    private static HolderSurfaceView mHolderSurfaceView = null;

    public static HolderSurfaceView getInstance() {
        if (mHolderSurfaceView == null)
            mHolderSurfaceView = new HolderSurfaceView();
        return mHolderSurfaceView;
    }

    public void setSurfaceView(SurfaceView view) {
        mSurfaceView = view;
        mSurfaceView.setZOrderOnTop(true);

        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public SurfaceView getSurfaceView() {
        return mSurfaceView;
    }
}