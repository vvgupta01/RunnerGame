package com.example.runner.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.view.GestureDetectorCompat;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,
        GestureDetector.OnGestureListener {
    public static final int WIDTH = 1280, HEIGHT = 720;
    private static float TOUCH_X, TOUCH_Y;
    public static final float SCALE_X = (float) WIDTH / Map.WIDTH,
        SCALE_Y = (float) HEIGHT / Map.HEIGHT;
    public static Matrix SCALE_MATRIX;

    private Context context;
    private GameThread thread;
    private GestureDetectorCompat detector;

    private GameState game;

    public GameView(Context context) {
        super(context);
        this.context = context;
        detector = new GestureDetectorCompat(context, this);

        setFocusable(true);
        getHolder().addCallback(this);
        getHolder().setFixedSize(WIDTH, HEIGHT);

        TOUCH_X = (float) WIDTH / MainActivity.WIDTH;
        TOUCH_Y = (float) HEIGHT / MainActivity.HEIGHT;
        SCALE_MATRIX = new Matrix();
        SCALE_MATRIX.postScale(SCALE_X, SCALE_Y);

        new Resources(context);
        new AudioPlayer(context, this);
    }

    public void onReady() {
        game = new GameState(context);
    }

    public void update() {
        if (game != null) {
            game.update();
        }

    }

    public void pause() {
        if (game != null) {
            game.pause();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (game != null) {
            game.draw(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GameThread(this, holder);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent me) {
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent me) {
        float tx = me.getRawX() * TOUCH_X;
        float ty = me.getRawY() * TOUCH_Y;
        game.tap(tx, ty);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent me1, MotionEvent me2, float velX, float velY) {
        float tx1 = me1.getRawX() * TOUCH_X;
        float ty1 = me1.getRawY() * TOUCH_Y;
        float tx2 = me2.getRawX() * TOUCH_X;
        float ty2 = me2.getRawY() * TOUCH_Y;
        game.swipe(tx1, ty1, tx2, ty2, velX, velY);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent me1, MotionEvent me2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent me) {

    }

    @Override
    public void onShowPress(MotionEvent me) {

    }
}
