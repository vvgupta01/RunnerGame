package com.example.runner.main;

public class Animation {
    private static final int TPF = 100;
    private int min, max, current;
    private double timer;
    private boolean active;

    public Animation(int min, int max) {
        this.min = min;
        this.max = max - 1;
        current = min;
    }

    public void update() {
        timer += 1000.0 / GameThread.FPS;
        if (timer >= TPF) {
            if (current == max) {
                current = min;
            } else {
                current++;
            }
            active = (current < max);
            timer = 0;
        }
    }

    public void start() {
        active = true;
    }

    public int getFrame() {
        return current;
    }

    public boolean isActive() {
        return active;
    }
}
