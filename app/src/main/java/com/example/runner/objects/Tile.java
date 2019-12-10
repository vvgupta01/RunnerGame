package com.example.runner.objects;

import com.example.runner.main.GameView;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;

public class Tile extends GameObject {
    public static final int HEIGHT = 16;
    public static final int GROUND_WIDTH = 96, GROUND_HEIGHT = 32;
    public static final int MAX_GROUND = Map.WIDTH / GROUND_WIDTH,
            MAX_TILES = 2;
    public static final int GROUND_Y = Map.HEIGHT - GROUND_HEIGHT;
    private static final float OFF_X = 2 * GameView.SCALE_X,
            OFF_Y = 2 * GameView.SCALE_Y;
    private boolean ground;

    public Tile(int id, float x, float y) {
       super(Resources.TILES[id], (id + 1) * 32, HEIGHT, x, y);
    }

    public Tile(float x) {
        super(Resources.TILES[3], GROUND_WIDTH, GROUND_HEIGHT, x, GROUND_Y);
        ground = true;
    }

    public void shift(float shift) { x -= shift; }

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

    public boolean isGround() { return ground; }
}
