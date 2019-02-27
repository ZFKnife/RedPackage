package com.sucisoft.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 2033152950
 * Created by zf on 2019/1/14.
 */

public class RedPackage2View extends View {
    private Context mContent;
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
    private int w;
    private int h;
    private Paint mPaint;
    private int position = 0;
    private Rect mDestRect = null;
    private Rect mSrcRect = null;
    private boolean isEventTrue = true;
    private Bitmap bitmap = null;
    private int delayMillis = 45;
    private float OffsetX = 100;
    private float OffsetY = 100;
    private int mClickBiaOffset = 50;

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
    private int[] mResIds = new int[]{
            R.mipmap.red_1,
            R.mipmap.red_2,
            R.mipmap.red_3,
            R.mipmap.red_4,
            R.mipmap.red_5
    };
    private boolean isBitmapOver = false;
    private boolean isRefreshStatus = false;
    private List<Bitmap> bitmapRed;
    private BitmapFactory.Options option;
    private Bitmap redBitmap = null;


    public RedPackage2View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPackage2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        this.mContent = context;
    }

    private void init() {
        position = 0;
        mPaint = new Paint();
        length = mImgResIds.length;
        isEventTrue = true;
        bitmapRed = new ArrayList<>();
        option = new BitmapFactory.Options();
        option.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterY = (float) (this.h * 0.3);
        mCenterX = (float) (this.w * 0.5);
        mTopCircleY = mCenterY - OffsetY;
        startX = 0;
        startY = (float) (mTopCircleY * 0.5);
        endX = w;
        endY = (float) (mTopCircleY * 0.5);
        leftX = startX + OffsetX;
        leftY = mTopCircleY;
        rightX = endX - OffsetX;
        rightY = mTopCircleY;
        for (int i = 0; i < mResIds.length; i++) {
            bitmapRed.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), mResIds[i], option), getWidth(), getHeight(), true));
        }
        redBitmap = createRedBitmap();
        initBitmapXY();
    }

    private void createBitmap() {
        if (position < length) {
            bitmap = BitmapFactory.decodeResource(getResources(), mImgResIds[position], option);
        }
        bitmapX = (float) (bitmap.getWidth() * 0.78);
        bitmapY = (float) (bitmap.getHeight() * 0.78);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) bitmapX, (int) bitmapY, true);
    }

    private double destDRectY;

    private void initBitmapXY() {
        if (bitmap == null || mSrcRect == null || !isBitmapOver) {
            createBitmap();
            destDRectY = (leftY - startY - bitmapY * 0.5) * 0.32;
            mSrcRect = new Rect(0, 0, (int) bitmapX, (int) bitmapY);
            mDestRect = new Rect((int) (mCenterX - bitmapX * 0.5), mDestTopScrollY(), (int) (mCenterX + bitmapX * 0.5), mDestBottomScrollY());
        }
    }

    private int mDestBottomScrollY() {
        return (int) (startY + bitmapY + destDRectY);
    }

    private int mDestTopScrollY() {
        return (int) (startY + destDRectY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.w = measureWidth(widthMeasureSpec);
        this.h = measureHeight(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.max(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.max(result, specSize);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(redBitmap, 0, 0, mPaint);
        if (!isBitmapOver && bitmap.isRecycled()) {
            initBitmapXY();
        }
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
        if (!isBitmapOver) {
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
            if (bitmap.isRecycled()) {
                initBitmapXY();
            }
            postInvalidate();
            if (position < length) {
                cyclePosition();
                if (checkBitmapRefresh()) {
                    isBitmapOver = true;
                    handler.postDelayed(mBitmapS, delayMillis);
                } else {
                    handler.postDelayed(mBitmapR, getDelayMillis());
                }
            }
        }
    };

    private Runnable mBitmapS = new Runnable() {
        @Override
        public void run() {
            redBitmap.recycle();
            redBitmap = createRedBitmap();
            postInvalidate();
            if (bitmapRed.size() > 0) {
                handler.postDelayed(mBitmapS, delayMillis);
                return;
            }
            if (onRedPackageOnclick != null) {
                onRedPackageOnclick.onEnd();
            }
        }
    };


    @Override
    protected void onDetachedFromWindow() {
        Log.i("----", "onDetachedFromWindow: ");
        handler.removeCallbacks(mBitmapR);
        handler.removeCallbacks(mBitmapS);
        bitmap.recycle();
        bitmap = null;
        redBitmap.recycle();
        redBitmap = null;
        for (int i = 0; i < bitmapRed.size(); i++) {
            bitmapRed.get(i).recycle();
            bitmapRed.get(i);
        }
        bitmapRed.clear();
        mContent = null;
        super.onDetachedFromWindow();
    }

    private Bitmap createRedBitmap() {
        return bitmapRed.remove(0);
    }


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


    private long getDelayMillis() {
        return (long) (delayMillis - position * 2.8);
    }

    private onRedPackageOnclick onRedPackageOnclick = null;

    public void setOnRedPackageOnclicking(onRedPackageOnclick onRedPackageOnclick) {
        this.onRedPackageOnclick = onRedPackageOnclick;
    }

    public interface onRedPackageOnclick {
        void onOpen();

        void onEnd();
    }

}
