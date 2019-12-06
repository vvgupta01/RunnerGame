package com.example.runner.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.runner.objects.Player;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    public static Matrix SCALE_MATRIX;
    public static float SCALE_X, SCALE_Y;
    public static boolean PAUSED;

    private GameThread thread;
    private Context context;

    private static Map map;
    private Player player;

    private int score;

    public GameSurface(Context context) {
        super(context);
        setFocusable(true);
        getHolder().addCallback(this);

        this.context = context;
        reset();
    }

    private void setScaleMatrix() {
        SCALE_X = MainActivity.WIDTH / 384f;
        SCALE_Y = MainActivity.HEIGHT / 216f;
        SCALE_MATRIX = new Matrix();
        SCALE_MATRIX.postScale(SCALE_X, SCALE_Y);
    }

    public void update() {
        if (!PAUSED) {
            player.update();
            if (player.getState() != Player.IDLE_STATE) {
                score++;
                map.update();
            }
        }

        if (getWidth() != MainActivity.WIDTH) {
            MainActivity.WIDTH = getWidth();
            reset();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        map.draw(canvas);
        player.draw(canvas);
        drawUI(canvas);
    }

    private void drawUI(Canvas canvas) {
        canvas.drawText("SCORE: " + score, 50, 100,
                Resources.getPaint(50, false));

        if (player.getState() == Player.IDLE_STATE) {
            canvas.drawText("TAP TO START", MainActivity.WIDTH / 2,
                    MainActivity.HEIGHT / 2, Resources.getPaint(75, true));
        }

        if (!PAUSED) {
            canvas.drawBitmap(Resources.ICONS[0], MainActivity.WIDTH - 106, 50,
                    null);
        } else {
            canvas.drawBitmap(Resources.ICONS[1], MainActivity.WIDTH - 106, 50,
                    null);
            canvas.drawText("PAUSED", MainActivity.WIDTH / 2, MainActivity.HEIGHT / 2,
                    Resources.getPaint(75, true));
        }
    }

    private void reset() {
        setScaleMatrix();
        new Resources(context);
        map = new Map();
        player = new Player();
    }

    public static Map getMap() {
        return map;
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
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            if (player.getState() == Player.IDLE_STATE) {
                player.run();
            } else {
                float x = me.getX();
                float y = me.getY();

                if (x > MainActivity.WIDTH - 106 && x < MainActivity.WIDTH - 50
                        && y > 50 && y < MainActivity.HEIGHT - 114) {
                    PAUSED = !PAUSED;
                } else if (!PAUSED && player.getState() != Player.JUMP_STATE){
                    player.jump();
                }
            }
            return true;
        }
        return false;
    }
}
