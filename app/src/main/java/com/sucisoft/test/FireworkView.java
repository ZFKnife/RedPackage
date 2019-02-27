package com.sucisoft.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FireworkView extends View {

    private final String TAG = this.getClass().getSimpleName();
    private LinkedList<Firework> fireworks = new LinkedList<Firework>();
    private int srceenWidth;
    private int screenHeight;
    private int bitmapColor[] = {
            R.mipmap.light_yellow, R.mipmap.light_red};
    private List<Bitmap> bitmaps;
    private BitmapFactory.Options option;

    public FireworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FireworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FireworkView(Context context) {
        super(context);
        init();
    }

    private void init() {
        option = new BitmapFactory.Options();
        option.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmaps = new ArrayList<>();
        for (int i = 0; i < bitmapColor.length; i++) {
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), bitmapColor[i], option);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 2, mBitmap.getHeight() * 2, true);
            bitmaps.add(mBitmap);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenHeight = h;
        srceenWidth = w;
    }

    public void start() {
        handler.postDelayed(runnable, 200);
    }

    private Random random = new Random();
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int w = random.nextInt(srceenWidth);
            int h = random.nextInt(screenHeight);
            lunchFireWork(w, h);
            handler.postDelayed(runnable, 400);
        }
    };


    public void lunchFireWork(float x, float y) {
        final Firework firework = new Firework(new Location(x, y), bitmaps);
        firework.addAnimationEndListener(new Firework.AnimationEndListener() {
            @Override
            public void onAnimationEnd(Firework mFirework) {
                fireworks.remove(mFirework);
                mFirework.clear();
                System.gc();
            }
        });
        fireworks.add(firework);
        firework.fire();
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < fireworks.size(); i++) {
            fireworks.get(i).draw(canvas);
        }
        if (fireworks.size() > 0)
            postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        for (int i = 0; i < bitmaps.size(); i++) {
            bitmaps.remove(i).recycle();
        }
        fireworks.clear();
        super.onDetachedFromWindow();
    }
}
