package com.example.runner.objects;

import android.util.Log;

import com.example.runner.main.GameThread;
import com.example.runner.main.GameView;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;

public class Tile extends GameObject {
    public static final int HEIGHT = 16;
    public static final int GROUND_WIDTH = 96, GROUND_HEIGHT = 32;

    public static final int MAX_GROUND = Map.WIDTH / GROUND_WIDTH;
    public static final int GROUND_Y = Map.HEIGHT - GROUND_HEIGHT;
    private static final float OFF_X = 2 * GameView.SCALE_X,
            OFF_Y = 2 * GameView.SCALE_Y;

    private float timer, ttl;
    private float velX, velY;
    private boolean ground;

    private Tile(int id, float x, float y) {
       super(Resources.TILES[id], (id + 1) * 32, HEIGHT, x, y);
    }

    public Tile(float x) {
        super(Resources.TILES[3], GROUND_WIDTH, GROUND_HEIGHT, x, GROUND_Y);
        ground = true;
    }

    public static Tile generateTile(int index) {
        float anchorX = Map.WIDTH + index * Map.WIDTH / Map.MAX_TILES;
        int marginX = (int) (Math.random() * Map.WIDTH / (2 * Map.MAX_TILES));

        float x = anchorX + marginX;
        float y = Tile.GROUND_Y - ((int) (Math.random() * 2) + 1)
                * (Player.HEIGHT + Tile.HEIGHT);
        int id = (int) (Math.random() * 3);
        return new Tile(id, x, y);
    }

    @Override
    public void update() {
        if (!isFalling()) {
            timer += GameThread.TPF;
        }

        if (timer >= ttl) {
            velY -= Map.GRAVITY;
            y -= velY;
        }
    }

    public void shift(float velX) {
        if (!isFalling() && this.velX != velX) {
            this.velX = velX;
            ttl = x / velX / GameThread.FPS * 1000;
//            Log.i("TTL", ttl+"");
        }
        x -= velX;
    }

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

    public boolean isFalling() { return (velY < 0); }
}
