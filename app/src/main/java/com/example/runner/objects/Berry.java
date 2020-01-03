package com.example.runner.objects;

import com.example.runner.main.GameThread;
import com.example.runner.main.GameView;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;

public class Berry extends GameObject {
    public static final int WIDTH = 14, HEIGHT = 15;
    private static final int FLOAT_TIME = 1000;

    private static final float MAX_SHIFT = Map.HEIGHT / 90f;
    public static final float SCALED_MAX_SHIFT = MAX_SHIFT * GameView.SCALE_Y;
    public static final float VELY = SCALED_MAX_SHIFT / GameThread.FPS * 2000 / FLOAT_TIME;

    private float velY = VELY;
    private final float startY;

    private Berry(float x, float y) {
        super(Resources.ITEMS[0], WIDTH, HEIGHT, x, y);
        startY = this.y;
    }

    public static Berry spawnBerry() {
        int level = (int) (Math.random() * 3);

        float x = Map.WIDTH;
        float y = Tile.GROUND_Y - level * (Player.HEIGHT + Tile.HEIGHT)
                - HEIGHT - MAX_SHIFT - 1;
        return new Berry(x, y);
    }

    @Override
    public void update() {
        y += velY;
        if (Math.abs(y - startY) >= SCALED_MAX_SHIFT) {
            velY *= -1;
        }
    }

    public void shift(float velX) {
        x -= velX;
    }
}
