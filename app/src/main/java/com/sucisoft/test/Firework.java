package com.sucisoft.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Firework {
    private final String TAG = this.getClass().getSimpleName();
    private final static int BIG_DEFAULT_ELEMENT_COUNT = 80;//大烟花爆炸数量
    private int BIG_DEFAULT_DURATION = 3000;
    private final static float BIG_DEFAULT_LAUNCH_SPEED = 6;
    private final static float DEFAULT_WIND_SPEED = 6;
    private final static float DEFAULT_GRAVITY = 6;

    private Paint mPaint;
    private int count; // count of element
    private int duration = 10000;
    private int colorPosition;
    private float launchSpeed;
    private float windSpeed;
    private float gravity;
    private int windDirection; // 1 or -1
    private Location location;
    private ValueAnimator animator;
    private float animatorValue;

    private ArrayList<Element> elements = new ArrayList<Element>();
    private AnimationEndListener listener;
    //大烟花颗粒随机图片
    private List<Bitmap> bitmaps;


    public Firework(Location location, List<Bitmap> bitmaps) {
        this.location = location;
        this.windDirection = 0;
        gravity = DEFAULT_GRAVITY;
        windSpeed = DEFAULT_WIND_SPEED;
        count = BIG_DEFAULT_ELEMENT_COUNT;
        duration = BIG_DEFAULT_DURATION;
        launchSpeed = BIG_DEFAULT_LAUNCH_SPEED;
        this.bitmaps = bitmaps;
        init();
    }

    private void init() {
        Random random = new Random(System.currentTimeMillis());
        elements.clear();
        for (int i = 0; i < count; i++) {
            colorPosition = random.nextInt(bitmaps.size());
            elements.add(new Element(colorPosition, Math.toRadians(random.nextInt(360)),
                    random.nextFloat() * launchSpeed));
        }
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        timeCount = 1;
        animatorValue = timeCount;
    }

    private float timeCount = 1;
    private final float dif = 0.00816f;

    float startTime;
    private boolean needRemove = false;

    public boolean getRemove() {
        return needRemove;
    }

    boolean isStart = false;

    /*
    * 开始烟花爆炸动画
    */
    public void fire() {
        animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(duration);
//从头开始动画
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorValue = (Float) valueAnimator.getAnimatedValue();
                isStart = true;
                for (Element element : elements) {
                    element.x = (float) (element.x
                            + Math.cos(element.direction) * element.speed
                            * animatorValue + windSpeed * windDirection);
                    element.y = (float) (element.y
                            - Math.sin(element.direction) * element.speed
                            * animatorValue + gravity * (1 - animatorValue));
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(Firework.this);
                needRemove = true;
            }
        });
        animator.start();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addAnimationEndListener(AnimationEndListener listener) {
        this.listener = listener;
    }

    private final int maxTime = 38;
    private int n = 0;

    public void draw(Canvas canvas) {
        mPaint.setAlpha((int) (225 * animatorValue));
        n++;
        if (n > maxTime) {
            listener.onAnimationEnd(Firework.this);
        }
        for (Element element : elements) {
            canvas.drawBitmap(bitmaps.get(element.colorPosition), location.x + element.x,
                    location.y + element.y, mPaint);
        }
        if (n > 2 && !isStart) {
            updateLocation();
        }
    }

    public void clear() {
        elements.clear();
    }


    public void updateLocation() {
        animatorValue -= dif;
        if (animatorValue < 0) {
            listener.onAnimationEnd(Firework.this);
        }
        for (Element element : elements) {
            element.x = (float) (element.x
                    + Math.cos(element.direction) * element.speed
                    * animatorValue + windSpeed * windDirection);
            element.y = (float) (element.y
                    - Math.sin(element.direction) * element.speed
                    * animatorValue + gravity * (1 - animatorValue));
        }
    }

    interface AnimationEndListener {
        void onAnimationEnd(Firework mFirework);
    }

}

