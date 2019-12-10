package com.example.runner.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.runner.states.Manager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1280, HEIGHT = 720;
    public static int SCREEN_WIDTH, SCREEN_HEIGHT;
    public static float TOUCH_X, TOUCH_Y;
    public static final float SCALE_X = (float) WIDTH / Map.WIDTH,
        SCALE_Y = (float) HEIGHT / Map.HEIGHT;
    public static Matrix SCALE_MATRIX;

    private GameThread thread;
    private Manager manager;

    public GameView(Context context) {
        super(context);
        setFocusable(true);
        getHolder().addCallback(this);

        SCALE_MATRIX = new Matrix();
        SCALE_MATRIX.postScale(SCALE_X, SCALE_Y);

        new Resources(context);
        manager = new Manager();
    }

    public void update() {
        manager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        manager.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        SCREEN_WIDTH = holder.getSurfaceFrame().width();
        SCREEN_HEIGHT = holder.getSurfaceFrame().height();
        TOUCH_X = (float) WIDTH / SCREEN_WIDTH;
        TOUCH_Y = (float) HEIGHT / SCREEN_HEIGHT;
        holder.setFixedSize(WIDTH, HEIGHT);

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
            float tx = me.getX() * TOUCH_X;
            float ty = me.getY() * TOUCH_Y;
            manager.input(tx, ty);
            return true;
        }
        return false;
    }
}
