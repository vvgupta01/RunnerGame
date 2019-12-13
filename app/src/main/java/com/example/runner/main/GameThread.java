package com.example.runner.main;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static int FPS = 30;
    public static float DELTA = 30f / FPS,
            TPF = 1000f / FPS;
    private boolean running;

    private final GameView surface;
    private final SurfaceHolder holder;

    GameThread(GameView surface, SurfaceHolder holder) {
        this.surface = surface;
        this.holder = holder;
    }

    @Override
    public void run() {
        Canvas canvas;
        long startTime, endTime, time, timer = 0L;
        int delay, frames = 0;

        while (running) {
            canvas = null;
            try {
                startTime = System.currentTimeMillis();
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    surface.update();
                    surface.draw(canvas);
                }
                endTime = System.currentTimeMillis();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            time = endTime - startTime;
            delay = (int) (TPF - time);
            int skippedFrames = 0;
            while (delay < 0 && skippedFrames < 5) {
                surface.update();
                delay += TPF;
                skippedFrames++;
            }

            try {
                sleep(delay);
            } catch (InterruptedException e) {
                //
            }

            timer += System.currentTimeMillis() - startTime;
            frames++;
            if (timer >= 1000) {
                Log.i("FPS", frames + "");
                DELTA = Math.max(30f / frames, 1);
                frames = 0;
                timer = 0;
            }
        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }
}
