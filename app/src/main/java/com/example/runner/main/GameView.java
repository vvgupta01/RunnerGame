package com.example.runner.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1280, HEIGHT = 720;
    private static float TOUCH_X, TOUCH_Y;
    public static final float SCALE_X = (float) WIDTH / Map.WIDTH,
        SCALE_Y = (float) HEIGHT / Map.HEIGHT;
    public static Matrix SCALE_MATRIX;

    private GameThread thread;
    private GameState game;

    public GameView(Context context) {
        super(context);
        setFocusable(true);
        getHolder().addCallback(this);
        getHolder().setFixedSize(WIDTH, HEIGHT);

        TOUCH_X = (float) WIDTH / MainActivity.WIDTH;
        TOUCH_Y = (float) HEIGHT / MainActivity.HEIGHT;

        SCALE_MATRIX = new Matrix();
        SCALE_MATRIX.postScale(SCALE_X, SCALE_Y);

        new Resources(context);
        game = new GameState();
    }

    public void update() {
        if (!MainActivity.PAUSED) {
            game.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        game.draw(canvas);
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
            } catch (InterruptedException e) {
                //
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            float tx = me.getRawX() * TOUCH_X;
            float ty = me.getRawY() * TOUCH_Y;
            game.input(tx, ty);
            return true;
        }
        return false;
    }
}
