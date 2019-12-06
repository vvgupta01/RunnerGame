package com.example.runner.objects;

import com.example.runner.main.GameSurface;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;

public class Ground extends GameObject {
    public static final int WIDTH = 96, HEIGHT = 32;
    public static final int MAX_TILES = Map.WIDTH / WIDTH;
    public static final float Y = 184 * GameSurface.SCALE_Y;

    private static final float OFF_Y = 2 * GameSurface.SCALE_Y;

    public Ground(float x) {
        super(Resources.GROUNDS[0], WIDTH, HEIGHT, x, Y);
    }

    public void shift(float shift) { x -= shift; }

    @Override
    public float getTop() {
        return y + OFF_Y;
    }
}
