package com.sucisoft.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 2033152950
 * Created by zf on 2019/1/14.
 */

public class RedPackageView extends View {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float leftX;
    private float leftY;
    private float rightX;
    private float rightY;
    private float mCenterY;
    private float mCenterX;
    private float mTopCircleY;
    private float bitmapX;
    private float bitmapY;
    private float w;
    private float h;
    private Paint mTopPaint;
    private Paint mBottomPaint;
    private Path mTopPath;
    private Path mBottomPath;
    private Paint mPaint;
    private int position = 0;
    private Rect mDestRect = null;
    private Rect mSrcRect = null;
    private int scrollY = 0;
    private boolean isEventTrue = true;
    private Bitmap bitmap = null;
    private int delayMillis = 45;
    private float scrollTopY = 0;
    private float scrollBottomY = 0;
    private float OffsetX = 100;
    private float OffsetY = 100;
    private int mClickBiaOffset = 50;
    private int redPacketTop = R.color.red_packet_top;
    private int redPacketBottom = R.color.red_packet_bottom;

    private int[] mImgResIds = new int[]{
            R.mipmap.icon_open_red_packet1,
            R.mipmap.icon_open_red_packet2,
            R.mipmap.icon_open_red_packet3,
            R.mipmap.icon_open_red_packet4,
            R.mipmap.icon_open_red_packet5,
            R.mipmap.icon_open_red_packet6,
            R.mipmap.icon_open_red_packet7,
            R.mipmap.icon_open_red_packet7,
            R.mipmap.icon_open_red_packet8,
            R.mipmap.icon_open_red_packet9,
            R.mipmap.icon_open_red_packet4,
            R.mipmap.icon_open_red_packet10,
            R.mipmap.icon_open_red_packet11,
    };
    private boolean isBitmapOver = false;
    private boolean isRefreshStatus = false;


    public RedPackageView(Context context) {
        super(context);
        init();
    }

    public RedPackageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RedPackageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        position = 0;
        scrollY = 0;
        mPaint = new Paint();
        mTopPaint = new Paint();
        mTopPaint.setColor(getResources().getColor(redPacketTop));
        mTopPaint.setAntiAlias(true);
        mTopPaint.setStyle(Paint.Style.FILL);
        mBottomPaint = new Paint();
        mBottomPaint.setColor(getResources().getColor(redPacketBottom));
        mBottomPaint.setAntiAlias(true);
        mBottomPaint.setStyle(Paint.Style.FILL);
        length = mImgResIds.length;
        isEventTrue = true;
    }

    private void createBitmap() {
        if (position < length) {
            bitmap = BitmapFactory.decodeResource(getResources(), mImgResIds[position]);
        }
        bitmapX = bitmap.getWidth();
        bitmapY = bitmap.getHeight();
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.h = h;
        this.w = w;
        //初始化坐标系
        mCenterY = (float) (h * 0.5);
        mCenterX = (float) (w * 0.5);
        mTopCircleY = mCenterY - OffsetY;
        startX = 0;
        startY = (float) (mTopCircleY * 0.5);
        endX = w;
        endY = (float) (mTopCircleY * 0.5);

        leftX = startX + OffsetX;
        leftY = mTopCircleY;
        rightX = endX - OffsetX;
        rightY = mTopCircleY;
        initPathXY();
        initBitmapXY();
    }

    private void initPathXY() {
        mTopPath = new Path();
        mBottomPath = new Path();
        mTopPath.moveTo(startX, startY - scrollTopY);
        mTopPath.cubicTo(leftX, leftY - scrollTopY, rightX, rightY - scrollTopY, endX, endY - scrollTopY);
        mTopPath.lineTo(endX, 0);
        mTopPath.lineTo(startX, 0);
        mTopPath.lineTo(startX, startY - scrollY);
        mBottomPath.moveTo(startX, startY + scrollBottomY);
        mBottomPath.cubicTo(leftX, leftY + scrollBottomY, rightX, rightY + scrollBottomY, endX, endY + scrollBottomY);
        mBottomPath.lineTo(endX, h);
        mBottomPath.lineTo(0, h);
        mBottomPath.lineTo(startX, h);
    }

    private double destDRectY;

    private void initBitmapXY() {
        if (bitmap == null || mSrcRect == null || !isBitmapOver) {
            createBitmap();
            destDRectY = (leftY - startY - bitmapY * 0.5) * 0.5;
            mSrcRect = new Rect(0, 0, (int) bitmapX, (int) bitmapY);
        }
        mDestRect = new Rect((int) (mCenterX - bitmapX * 0.5), mDestTopScrollY(), (int) (mCenterX + bitmapX * 0.5), mDestBottomScrollY());
    }

    private int mDestBottomScrollY() {
        if (isBitmapOver) {
            return (int) (startY + bitmapY + destDRectY - scrollTopY);
        } else {
            return (int) (startY + bitmapY + destDRectY);
        }
    }

    private int mDestTopScrollY() {
        if (isBitmapOver) {
            return (int) (startY + destDRectY - scrollTopY);
        } else {
            return (int) (startY + destDRectY);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mTopPath, mTopPaint);
        canvas.drawPath(mBottomPath, mBottomPaint);
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
        if (isBitmapOver) {
            mTopPath.close();
            mBottomPath.close();
        } else {
            bitmap.recycle();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isEventTrue) {
                    float eventX = event.getX();
                    float eventY = event.getY();
                    if (checkEvent(eventX, eventY)) {
                        scrollY = 0;
                        isEventTrue = false;
                        handler.postDelayed(mBitmapR, delayMillis);
                        if (onRedPackageOnclick != null) {
                            onRedPackageOnclick.onOpen();
                        }
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean checkEvent(float eventX, float eventY) {
        return onEventLeft() < eventX && onEventRight() > eventX && onEventTop() < eventY && onEventBottom() > eventY;
    }

    private float onEventBottom() {
        return startY + bitmapY + (leftY - startY) / 3 + mClickBiaOffset;
    }

    private float onEventTop() {
        return startY + (leftY - startY) / 3 - mClickBiaOffset;
    }

    private float onEventRight() {
        return mCenterX + bitmapX + mClickBiaOffset;
    }

    private double onEventLeft() {
        return mCenterX - bitmapX - mClickBiaOffset;
    }

    public void onRefreshStatus() {
        isRefreshStatus = true;
    }

    private Handler handler = new Handler();
    private int length = 0;
    private Runnable mBitmapR = new Runnable() {
        @Override
        public void run() {
            initBitmapXY();
            invalidate();
            if (position < length) {
                cyclePosition();
                if (checkBitmapRefresh()) {
                    isBitmapOver = true;
                    handler.postDelayed(mRedOpenR, (long) (delayMillis * 0.5));
                    return;
                }
                handler.postDelayed(mBitmapR, getDelayMillis());
            }
        }
    };

    private void cyclePosition() {
        if (position + 1 == length) {
            position = 0;
        } else {
            position++;
        }
    }

    private boolean checkBitmapRefresh() {
        return position == 1 && isRefreshStatus;
    }

    private Runnable mRedOpenR = new Runnable() {
        @Override
        public void run() {
            position++;
            calculatedOffsetY();
            initPathXY();
            initBitmapXY();
            invalidate();
            if (position < length) {
                handler.postDelayed(mRedOpenR, (long) (delayMillis * 0.5));
                return;
            }
            position = 0;
        }
    };

    private void calculatedOffsetY() {
        scrollY += 10;
        scrollTopY = (float) (scrollY * 0.8);
        scrollBottomY = (float) (scrollY * 10);
    }

    private long getDelayMillis() {
        return (long) (delayMillis - position * 2.8);
    }

    private onRedPackageOnclick onRedPackageOnclick = null;

    public void setOnRedPackageOnclicking(onRedPackageOnclick onRedPackageOnclick) {
        this.onRedPackageOnclick = onRedPackageOnclick;
    }

    public interface onRedPackageOnclick {
        void onOpen();
    }
}
