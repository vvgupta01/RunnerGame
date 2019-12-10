package com.example.runner.objects;

import com.example.runner.main.Resources;

public class Button extends GameObject {
    public static final int WIDTH = 16, HEIGHT = 16;
    private boolean clicked;

    public Button(int id, float x, float y) {
        super(Resources.ICONS[id], WIDTH, HEIGHT, x, y);
    }

    public boolean click(float tx, float ty) {
        boolean isClicked = (tx >= x && tx <= x + width && ty >= y && ty <= y + height);
        if (isClicked) {
            clicked = !clicked;
        }
        return isClicked;
    }

    public boolean isClicked() {
        return clicked;
    }
}