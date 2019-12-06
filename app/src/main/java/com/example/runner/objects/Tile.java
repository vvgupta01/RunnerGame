package com.example.runner.objects;

import com.example.runner.main.GameSurface;
import com.example.runner.main.Resources;

public class Tile extends GameObject {
    public static final int HEIGHT = 16;
    private static final float OFF_X = 2 * GameSurface.SCALE_X,
            OFF_Y = 2 * GameSurface.SCALE_Y;

    public Tile(int id, float x, float y) {
       super(Resources.TILES[id], (id + 1) * 32, HEIGHT, x, y);
    }

    public void shift(float shift) { x -= shift; }

    public boolean isOffScreen() { return (x + width <= 0); }

    @Override
    public float getLeft() {
        return x + OFF_X;
    }

    @Override
    public float getRight() {
        return super.getRight() - OFF_X;
    }

    @Override
    public float getTop() {
        return y + OFF_Y;
    }

    @Override
    public float getBottom() {
        return super.getBottom() - OFF_Y;
    }
}
