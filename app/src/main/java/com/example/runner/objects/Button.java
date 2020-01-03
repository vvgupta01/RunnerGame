package com.example.runner.objects;

import android.graphics.Canvas;

import com.example.runner.main.Map;
import com.example.runner.main.Resources;

public class Button extends GameObject {
    public static final int WIDTH = 16, HEIGHT = 16;
    public static final int PAUSE = 0, PLAY = 1, RESET = 2, HELP = 3;
    private int state;

    public Button(float x, float y) {
        super(null, WIDTH, HEIGHT, x, y);
    }

    public boolean click(float tx, float ty) {
        return (tx >= x && tx <= x + width && ty >= y && ty <= y + height);
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(Resources.ICONS[state], x, y, null);
    }
}