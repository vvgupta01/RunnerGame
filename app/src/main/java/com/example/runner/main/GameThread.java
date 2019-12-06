package com.example.runner.main;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static final int FPS = 30;
    private static final int TPF = 1000 / FPS;
    private boolean running;

    private final GameSurface surface;
    private final SurfaceHolder holder;

    GameThread(GameSurface surface, SurfaceHolder holder) {
        this.surface = surface;
        this.holder = holder;
    }

    @Override
    public void run() {
        Canvas canvas;
        long startTime, endTime;
        int delay, skippedFrames;

        while (running) {
            canvas = null;
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    skippedFrames = 0;
                    startTime = System.currentTimeMillis();
                    surface.update();
                    surface.draw(canvas);
                    endTime = System.currentTimeMillis();

                    delay = (int) (TPF - (endTime - startTime));
                    if (delay > 0) {
                        try {
                            sleep(delay);
                        } catch (InterruptedException e) {
                            //
                        }
                    }

                    while (delay < 0 && skippedFrames < 5) {
                        surface.update();
                        delay += TPF;
                        skippedFrames++;
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }
}
